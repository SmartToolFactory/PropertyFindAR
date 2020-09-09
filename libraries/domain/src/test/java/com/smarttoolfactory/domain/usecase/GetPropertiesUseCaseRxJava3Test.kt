package com.smarttoolfactory.domain.usecase

import android.database.SQLException
import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.repository.PropertyRepositoryRxJava3
import com.smarttoolfactory.domain.error.EmptyDataException
import com.smarttoolfactory.domain.mapper.PropertyEntityToItemListMapper
import com.smarttoolfactory.test_utils.extension.RxImmediateSchedulerExtension
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.RegisterExtension

/**
 * Used single instance of tests instead of creating new instance for each test.
 *
 * @see <a href="https://phauer.com/2018/best-practices-unit-testing-kotlin/">Kotlin Test Performance</a>
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetPropertiesUseCaseRxJava3Test {

    private val repository: PropertyRepositoryRxJava3 = mockk()
    private val mapper: PropertyEntityToItemListMapper = mockk()

    companion object {

        @JvmField
        @RegisterExtension
        val rxImmediateSchedulerExtension = RxImmediateSchedulerExtension()

        private val entityList = TestData.entityList

        private val itemList = TestData.itemList
    }

    private lateinit var useCase: GetPropertiesUseCaseRxJava3

    /***
     *
     *   Test class for testing offline-last data fetch with [GetPropertiesUseCaseRxJava3]
     *
     *  * Offline Last Scenarios
     *
     * * Check out Remote Source first
     * * If empty data or null returned throw empty set exception
     * * If error occurred while fetching data from remote: Try to fetch data from db
     * * If data is fetched from remote source: delete old data, save new data and return new data
     * * If both network and db don't have any data throw empty set exception
     *
     */
    @Nested
    @DisplayName("Offline-Last(Refresh) Tests")
    inner class OffLineLastTest {

        @Test
        fun `given data returned from Remote, Local should delete old, save and return data`() {

            // GIVEN
            every { repository.fetchEntitiesFromRemote() } returns Single.just(entityList)
            every { repository.deletePropertyEntities() } returns Completable.complete()
            every {
                repository.savePropertyEntities(propertyEntities = entityList)
            } returns Completable.complete()
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(entityList)
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.fetchEntitiesFromRemote()
                repository.deletePropertyEntities()
                repository.savePropertyEntities(entityList)
                repository.getPropertyEntitiesFromLocal()
                mapper.map(entityList)
            }
        }

        @Test
        fun `given exception returned from Remote source, should fetch data from Local source`() {

            // GIVEN
            every {
                repository.fetchEntitiesFromRemote()
            } returns Single.error(Exception("Network Exception"))
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(entityList)
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.fetchEntitiesFromRemote()
                repository.getPropertyEntitiesFromLocal()
                mapper.map(entityList)
            }
        }

        @Test
        fun `given empty data or null returned from Remote, should fetch data from Local `() {

            // GIVEN
            every { repository.fetchEntitiesFromRemote() } returns Single.just(listOf())
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(entityList)
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.fetchEntitiesFromRemote()
                repository.getPropertyEntitiesFromLocal()
                mapper.map(entityList)
            }
        }

        @Test
        fun `given exception returned from Local source, should throw DB exception`() {

            // GIVEN
            every {
                repository.fetchEntitiesFromRemote()
            } returns Single.error(Exception("Network Exception"))

            every {
                repository.getPropertyEntitiesFromLocal()
            } returns Single.error(SQLException("Database Exception"))

            // WHEN
            val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertNotComplete()
                .assertError(SQLException::class.java)
                .dispose()

            verifySequence {
                repository.fetchEntitiesFromRemote()
                repository.getPropertyEntitiesFromLocal()
            }
        }

        @Test
        fun `given Remote error and Local source is empty, should throw EmptyDataException`() {

            // GIVEN
            every {
                repository.fetchEntitiesFromRemote()
            } returns Single.error(Exception("Network Exception"))

            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(listOf())

            // WHEN
            val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test()

            // THEN
            testObserver

                .assertError(EmptyDataException::class.java)
                .dispose()

            verifySequence {
                repository.fetchEntitiesFromRemote()
                repository.getPropertyEntitiesFromLocal()
            }
        }
    }

    @Nested
    @DisplayName("Offline-First Tests")
    inner class OffLineFirstTest {

        @Test
        fun `given Local source has data, should return data`() {

            // GIVEN
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(entityList)
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.getPropertyEntitiesFromLocal()
                mapper.map(entityList)
            }
        }

        @Test
        fun `given Local source is empty, should fetch data from Remote`() {

            // GIVEN
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(listOf())
            every { repository.fetchEntitiesFromRemote() } returns Single.just(entityList)
            every { repository.deletePropertyEntities() } returns Completable.complete()
            every {
                repository.savePropertyEntities(propertyEntities = entityList)
            } returns Completable.complete()
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.getPropertyEntitiesFromLocal()
                repository.fetchEntitiesFromRemote()
                repository.deletePropertyEntities()
                repository.savePropertyEntities(propertyEntities = entityList)
                mapper.map(entityList)
            }
        }

        @Test
        fun `given exception returned from Local source should fetch data from Remote`() {

            // GIVEN
            every {
                repository.getPropertyEntitiesFromLocal()
            } returns Single.error(SQLException("Database Exception"))
            every { repository.fetchEntitiesFromRemote() } returns Single.just(entityList)
            every { repository.deletePropertyEntities() } returns Completable.complete()
            every {
                repository.savePropertyEntities(propertyEntities = entityList)
            } returns Completable.complete()
            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertComplete()
                .assertNoErrors()
                .assertValue {
                    it.containsAll(itemList)
                }
                .dispose()

            verifySequence {
                repository.getPropertyEntitiesFromLocal()
                repository.fetchEntitiesFromRemote()
                repository.deletePropertyEntities()
                repository.savePropertyEntities(propertyEntities = entityList)
                mapper.map(entityList)
            }
        }

        @Test
        fun `given Local source is empty and Remote returned error, should throw exception`() {

            // GIVEN
            every { repository.getPropertyEntitiesFromLocal() } returns Single.just(listOf())

            every {
                repository.fetchEntitiesFromRemote()
            } returns Single.error(Exception("Network Exception"))

            every { mapper.map(entityList) } returns itemList

            // WHEN
            val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test()

            // THEN
            testObserver
                .assertNotComplete()
                .assertError(EmptyDataException::class.java)
                .dispose()

            verifySequence {
                repository.getPropertyEntitiesFromLocal()
                repository.fetchEntitiesFromRemote()
            }
        }
    }

    @BeforeEach
    fun setUp() {
        useCase = GetPropertiesUseCaseRxJava3(repository, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(repository, mapper)
    }
}
