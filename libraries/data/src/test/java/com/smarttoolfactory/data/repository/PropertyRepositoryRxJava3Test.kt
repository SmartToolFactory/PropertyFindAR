package com.smarttoolfactory.data.repository

import com.google.common.truth.Truth
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_1
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_2
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PropertyRepositoryRxJava3Test {

    private lateinit var repository: PropertyRepositoryRxJava3

    private val localDataSource: LocalPropertyDataSourceRxJava3 = mockk()
    private val remoteDataSource: RemotePropertyDataSourceRxJava3 = mockk()
    private val mapper: PropertyDTOtoEntityListMapper = mockk()

    companion object {

        private val propertyResponse = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!

        private val propertyDTOList = propertyResponse.res

        private val entityList =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(propertyDTOList)

        // FIXME Cannot convert from Json to Entity even with wrapper, check out Moshi or Jackson

        private val propertyResponsePage1 = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
        )!!

        private val propertyResponsePage2 = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH_PAGE_2)
        )!!

        private val propertyDTOListPage1 = propertyResponsePage1.res
        private val propertyDTOListPage2 = propertyResponsePage2.res

        private val entityListPage1 =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(
                    convertToObjectFromJson<PropertyResponse>(
                        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
                    )!!.res
                )

        private val entityListPage2 =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(
                    convertToObjectFromJson<PropertyResponse>(
                        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
                    )!!.res
                )
    }

    @Test
    fun `given network error occurred, should throw Exception`() {

        // GIVEN
        every {
            remoteDataSource.getPropertyDTOs()
        } returns Single.error(Exception("Network Exception"))
        every { mapper.map(propertyDTOList) } returns entityList

        // WHEN
        val testObserver = repository.fetchEntitiesFromRemote().test()

        // THEN
        testObserver
            .assertError(Exception::class.java)
            .assertError {
                it.message == "Network Exception"
            }
            .assertNotComplete()
            .dispose()

        verify(exactly = 1) { remoteDataSource.getPropertyDTOs() }
        verify(exactly = 0) { mapper.map(propertyDTOList) }
    }

    @Test
    fun `given remote data source return PropertyDTO list, should return PropertyEntity list`() {

        // GIVEN
        every { remoteDataSource.getPropertyDTOs() } returns Single.just(propertyDTOList)
        every { mapper.map(propertyDTOList) } returns entityList

        // WHEN
        val expected = repository.fetchEntitiesFromRemote().blockingGet()

        // THEN
        Truth.assertThat(expected).containsExactlyElementsIn(entityList)
        verifyOrder {
            remoteDataSource.getPropertyDTOs()
            mapper.map(propertyDTOList)
        }
    }

    @Test
    fun `given page 2 returned data returned should have current page number 2 with Pagination`() {

        // GIVEN

        // Page 1 Pagination
        val page1DTO = propertyDTOListPage1
        val page1Data = entityListPage1

        coEvery {
            remoteDataSource.getPropertyDTOsWithPagination(1)
        } returns Single.just(page1DTO)

        every { mapper.map(page1DTO) } returns page1Data

        // Page 2 Pagination
        val page2DTO = propertyDTOListPage2
        val page2Data = entityListPage2

        coEvery {
            remoteDataSource.getPropertyDTOsWithPagination(2)
        } returns Single.just(page2DTO)

        every { mapper.map(page2DTO) } returns page2Data

        // WHEN
        val expected1 = repository
            .fetchEntitiesFromRemoteByPage(1)
            .blockingGet()

        val page1 = repository.getCurrentPageNumber()

        val expected2 = repository
            .fetchEntitiesFromRemoteByPage(2)
            .blockingGet()
        val page2 = repository.getCurrentPageNumber()

        // THEN
        Truth.assertThat(expected1).isEqualTo(page1Data)
        Truth.assertThat(page1).isEqualTo(1)

        Truth.assertThat(expected2).isEqualTo(page2Data)
        Truth.assertThat(page2).isEqualTo(2)

        coVerifyOrder {
            remoteDataSource.getPropertyDTOsWithPagination(1)
            mapper.map(page1DTO)
            remoteDataSource.getPropertyDTOsWithPagination(2)
            mapper.map(page2DTO)
        }
    }

    @Test
    fun `given DB is empty should return an empty list`() {

        // GIVEN
        val expected = listOf<PropertyEntity>()
        every { localDataSource.getPropertyEntities() } returns Single.just(expected)

        // WHEN
        val actual = repository.getPropertyEntitiesFromLocal().blockingGet()

        // THEN
        Truth.assertThat(actual).isEmpty()
        verify(exactly = 1) { localDataSource.getPropertyEntities() }
    }

    @Test
    fun `given DB is populated should return data list`() {

        // GIVEN
        every { localDataSource.getPropertyEntities() } returns Single.just(entityList)

        // WHEN
        val actual = repository.getPropertyEntitiesFromLocal().blockingGet()

        // THEN
        Truth.assertThat(actual)
            .containsExactlyElementsIn(entityList)
        verify(exactly = 1) { localDataSource.getPropertyEntities() }
    }

    @Test
    fun `given entities, should save entities`() {

        // GIVEN
        every { localDataSource.saveEntities(entityList) } returns Completable.complete()

        // WHEN
        val testObserver = repository.savePropertyEntities(entityList).test()

        // THEN
        testObserver.assertNoErrors()
            .assertComplete()
            .dispose()
        verify(exactly = 1) { localDataSource.saveEntities(entityList) }
    }

    @Test
    fun `given no error should delete entities`() {

        // GIVEN
        every { localDataSource.deletePropertyEntities() } returns Completable.complete()

        // WHEN
        val testObserver = repository.deletePropertyEntities().test()

        // THEN
        testObserver.assertNoErrors()
            .assertComplete()
            .dispose()
        verify(exactly = 1) { localDataSource.deletePropertyEntities() }
    }

    @BeforeEach
    fun setUp() {
        repository = PropertyRepositoryImlRxJava3(localDataSource, remoteDataSource, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(localDataSource, remoteDataSource, mapper)
    }
}
