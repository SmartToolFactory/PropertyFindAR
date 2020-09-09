package com.smarttoolfactory.data.source

import com.google.common.truth.Truth
import com.smarttoolfactory.data.api.PropertyApiRxJava
import com.smarttoolfactory.data.db.PropertyDaoRxJava3
import com.smarttoolfactory.data.db.SortOrderDaoRxJava3
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Used single instance of tests instead of creating new instance for each test.
 *
 * @see <a href="https://phauer.com/2018/best-practices-unit-testing-kotlin/">Kotlin Test Performance</a>
 *
 */
class PropertyDataSourceRxJava3Test {

    companion object {

        private val propertyResponse = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!

        private val propertyDTOList = propertyResponse.res

        private val entityList =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(propertyDTOList)
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RemoteDataSourceTest {

        private val api = mockk<PropertyApiRxJava>()

        private lateinit var remoteDataSource: RemoteDataSourceRxJava3Impl

        @Test
        fun `given network error occurred, should return Single with error`() {

            // GIVEN
            every { api.getPropertyResponse() } returns Single.error(Exception("Network Exception"))

            // WHEN
            val testObserver = remoteDataSource.getPropertyDTOs().test()

            // THEN
            testObserver.assertError {
                it.message == "Network Exception"
            }
            verify(exactly = 1) { api.getPropertyResponse() }
            testObserver.dispose()
        }

        @Test
        fun `given Http 200, should return DTO list`() {

            // GIVEN
            val actual = propertyDTOList
            every { api.getPropertyResponse() } returns Single.just(propertyResponse)

            // WHEN
            val expected = remoteDataSource.getPropertyDTOs().blockingGet()

            // THEN
            Truth.assertThat(expected).isEqualTo(actual)
            verify(exactly = 1) { api.getPropertyResponse() }
        }

        @BeforeEach
        fun setUp() {
            remoteDataSource = RemoteDataSourceRxJava3Impl(api)
        }

        @AfterEach
        fun tearDown() {
            clearMocks(api)
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class LocalDataSourceTest {

        private val dao = mockk<PropertyDaoRxJava3>()
        private val sortDao = mockk<SortOrderDaoRxJava3>()

        private lateinit var localDataSource: LocalDataSourceRxJava3Impl

        @Test
        fun `given DB is empty should return an empty list`() = runBlockingTest {

            // GIVEN
            val expected = listOf<PropertyEntity>()
            every { dao.getPropertiesSingle() } returns Single.just(expected)

            // WHEN
            val actual = localDataSource.getPropertyEntities().blockingGet()

            // THEN
            Truth.assertThat(actual).isEmpty()
            verify(exactly = 1) { dao.getPropertiesSingle() }
        }

        @Test
        fun `given DB is populated should return data list`() = runBlockingTest {

            // GIVEN
            every { dao.getPropertiesSingle() } returns Single.just(entityList)

            // WHEN
            val actual = localDataSource.getPropertyEntities().blockingGet()

            // THEN
            Truth.assertThat(actual)
                .containsExactlyElementsIn(entityList)
            verify(exactly = 1) { dao.getPropertiesSingle() }
        }

        @Test
        fun `given entities, should save entities to DB`() = runBlockingTest {

            // GIVEN
            every { dao.insert(entityList) } returns Completable.complete()

            // WHEN
            val testObserver = localDataSource.saveEntities(entityList).test()

            // THEN
            testObserver.assertComplete()
                .assertNoErrors()
                .dispose()
            verify(exactly = 1) { dao.insert(entityList) }
        }

        @Test
        fun `given no error should delete entities`() = runBlockingTest {

            // GIVEN
            every { dao.deleteAll() } returns Completable.complete()

            // WHEN
            val testObserver = localDataSource.deletePropertyEntities().test()

            // THEN
            testObserver.assertComplete()
                .assertNoErrors()
                .dispose()
            verify(exactly = 1) { dao.deleteAll() }
        }

        @BeforeEach
        fun setUp() {
            localDataSource = LocalDataSourceRxJava3Impl(dao, sortDao)
        }

        @AfterEach
        fun tearDown() {
            clearMocks(dao, sortDao)
        }
    }
}
