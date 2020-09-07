package com.smarttoolfactory.domain.usecase

import android.database.SQLException
import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.data.repository.PropertyRepositoryCoroutines
import com.smarttoolfactory.domain.dispatcher.UseCaseDispatchers
import com.smarttoolfactory.domain.error.EmptyDataException
import com.smarttoolfactory.domain.mapper.PropertyEntityToItemListMapper
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.extension.TestCoroutineExtension
import com.smarttoolfactory.test_utils.test_observer.test
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
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
class GetPropertiesUseCaseFlowTest {

    private val repository: PropertyRepositoryCoroutines = mockk()
    private val entityToPropertyMapper: PropertyEntityToItemListMapper = mockk()

    private val dispatcherProvider: UseCaseDispatchers =
        UseCaseDispatchers(Dispatchers.Main, Dispatchers.Main, Dispatchers.Main)

    companion object {
        @JvmField
        @RegisterExtension
        val testCoroutineExtension = TestCoroutineExtension()

        private val propertyResponse = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!

        private val entityList = TestData.entityList

        private val itemList = TestData.itemList
    }

    private lateinit var useCase: GetPropertiesUseCaseFlow

    /***
     *
     *   Test class for testing offline-last data fetch with [GetPropertiesUseCaseFlow]
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
        fun `given data returned from Remote, Local should delete old, save and return data`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.fetchEntitiesFromRemote() } returns entityList
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery {
                    repository.savePropertyEntities(propertyEntities = entityList)
                } just runs
                coEvery { repository.getPropertyEntitiesFromLocal() } returns entityList
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValueAt(0, itemList)
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerifyOrder {
                    repository.fetchEntitiesFromRemote()
                    repository.deletePropertyEntities()
                    repository.savePropertyEntities(entityList)
                    repository.getPropertyEntitiesFromLocal()
                    entityToPropertyMapper.map(entityList)
                }
            }

        @Test
        fun `given exception returned from Remote source, should fetch data from Local source`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery {
                    repository.fetchEntitiesFromRemote()
                } throws Exception("Network Exception")
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery {
                    repository.savePropertyEntities(propertyEntities = entityList)
                } just runs
                coEvery { repository.getPropertyEntitiesFromLocal() } returns entityList
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver =
                    useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerify(exactly = 1) { repository.fetchEntitiesFromRemote() }
                coVerify(exactly = 1) { repository.getPropertyEntitiesFromLocal() }
                coVerify(exactly = 0) { repository.deletePropertyEntities() }
                coVerify(exactly = 0) { repository.savePropertyEntities(entityList) }
                verify(exactly = 1) { entityToPropertyMapper.map(entityList) }
            }

        @Test
        fun `given empty data or null returned from Remote, should fetch data from Local `() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.fetchEntitiesFromRemote() } returns listOf()
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery {
                    repository.savePropertyEntities(propertyEntities = entityList)
                } just runs
                coEvery { repository.getPropertyEntitiesFromLocal() } returns entityList
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerify(exactly = 1) { repository.fetchEntitiesFromRemote() }
                coVerify(exactly = 1) { repository.getPropertyEntitiesFromLocal() }
                coVerify(exactly = 0) { repository.deletePropertyEntities() }
                coVerify(exactly = 0) { repository.savePropertyEntities(entityList) }

                verify(exactly = 1) { entityToPropertyMapper.map(entityList) }
            }

        @Test
        fun `given exception returned from Local source, should throw DB exception`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery {
                    repository.fetchEntitiesFromRemote()
                } throws Exception("Network Exception")
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery {
                    repository.savePropertyEntities(propertyEntities = entityList)
                } just runs
                coEvery {
                    repository.getPropertyEntitiesFromLocal()
                } throws SQLException("Database Exception")
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertNotComplete()
                    .assertError(SQLException::class.java)
                    .dispose()

                coVerify(exactly = 1) { repository.fetchEntitiesFromRemote() }
                coVerify(exactly = 1) { repository.getPropertyEntitiesFromLocal() }
                coVerify(exactly = 0) { repository.deletePropertyEntities() }
                coVerify(exactly = 0) { repository.savePropertyEntities(entityList) }

                verify(exactly = 0) { entityToPropertyMapper.map(entityList) }
            }

        @Test
        fun `given Remote error and Local source is empty, should throw EmptyDataException`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery {
                    repository.fetchEntitiesFromRemote()
                } throws Exception("Network Exception")

                coEvery { repository.deletePropertyEntities() } just runs
                coEvery {
                    repository.savePropertyEntities(propertyEntities = entityList)
                } just runs
                coEvery { repository.getPropertyEntitiesFromLocal() } returns listOf()

                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineLast(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertNotComplete()
                    .assertError(EmptyDataException::class.java)
                    .dispose()

                coVerify(exactly = 1) { repository.fetchEntitiesFromRemote() }
                coVerify(exactly = 1) { repository.getPropertyEntitiesFromLocal() }
                coVerify(exactly = 0) { repository.deletePropertyEntities() }
                coVerify(exactly = 0) { repository.savePropertyEntities(entityList) }

                verify(exactly = 0) { entityToPropertyMapper.map(entityList) }
            }
    }

    @Nested
    @DisplayName("Offline-First Tests")
    inner class OffLineFirstTest {

        @Test
        fun `given Local source has data, should return data`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.getOrderFilter() } returns ORDER_BY_NONE
                coEvery { repository.getPropertyEntitiesFromLocal() } returns entityList
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList
                // WHEN
                val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerifySequence {
                    repository.getOrderFilter()
                    repository.getPropertyEntitiesFromLocal()
                    entityToPropertyMapper.map(entityList)
                }
            }

        @Test
        fun `given Local source is empty, should fetch data from Remote`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.getOrderFilter() } returns ORDER_BY_NONE
                coEvery { repository.getPropertyEntitiesFromLocal() } returns listOf()
                coEvery { repository.fetchEntitiesFromRemote() } returns entityList
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery { repository.savePropertyEntities(propertyEntities = entityList) } just runs
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList
                // WHEN
                val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerifySequence {
                    repository.getOrderFilter()
                    repository.getPropertyEntitiesFromLocal()
                    repository.fetchEntitiesFromRemote()
                    repository.deletePropertyEntities()
                    repository.savePropertyEntities(propertyEntities = entityList)
                    entityToPropertyMapper.map(entityList)
                }
            }

        @Test
        fun `given exception returned from Local source should fetch data from Remote`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.getOrderFilter() } returns ORDER_BY_NONE
                coEvery {
                    repository.getPropertyEntitiesFromLocal()
                } throws SQLException("Database Exception")
                coEvery { repository.fetchEntitiesFromRemote() } returns entityList
                coEvery { repository.deletePropertyEntities() } just runs
                coEvery { repository.savePropertyEntities(propertyEntities = entityList) } just runs
                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertComplete()
                    .assertNoErrors()
                    .assertValues {
                        it.first().containsAll(itemList)
                    }
                    .dispose()

                coVerifySequence {
                    repository.getOrderFilter()
                    repository.getPropertyEntitiesFromLocal()
                    repository.fetchEntitiesFromRemote()
                    repository.deletePropertyEntities()
                    repository.savePropertyEntities(propertyEntities = entityList)
                    entityToPropertyMapper.map(entityList)
                }
            }

        @Test
        fun `given Local source is empty and Remote returned error, should throw exception`() =
            testCoroutineExtension.runBlockingTest {

                // GIVEN
                coEvery { repository.getOrderFilter() } returns ORDER_BY_NONE
                coEvery { repository.getPropertyEntitiesFromLocal() } returns listOf()

                coEvery {
                    repository.fetchEntitiesFromRemote()
                } throws Exception("Network Exception")

                coEvery { entityToPropertyMapper.map(entityList) } returns itemList

                // WHEN
                val testObserver = useCase.getPropertiesOfflineFirst(ORDER_BY_NONE).test(this)

                // THEN
                testObserver
                    .assertNotComplete()
                    .assertError(EmptyDataException::class.java)
                    .dispose()

                coVerifySequence {
                    repository.getOrderFilter()
                    repository.getPropertyEntitiesFromLocal()
                    repository.fetchEntitiesFromRemote()
                }
            }
    }

    @BeforeEach
    fun setUp() {
        useCase = GetPropertiesUseCaseFlow(
            repository,
            entityToPropertyMapper,
            dispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        clearMocks(repository, entityToPropertyMapper)
    }
}
