package com.smarttoolfactory.data.repository

import com.google.common.truth.Truth
import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.data.source.LocalPropertyDataSourceCoroutines
import com.smarttoolfactory.data.source.RemotePropertyDataSourceCoroutines
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_1
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_2
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PropertyRepositoryCoroutinesTest {

    private lateinit var repository: PropertyRepositoryCoroutines

    private val localDataSource: LocalPropertyDataSourceCoroutines = mockk()
    private val remoteDataSource: RemotePropertyDataSourceCoroutines = mockk()
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
    fun `given network error occurred, should throw Exception`() = runBlockingTest {

        // GIVEN
        coEvery { remoteDataSource.getPropertyDTOs() } throws Exception("Network Exception")
        every { mapper.map(propertyDTOList) } returns entityList

        // WHEN
        val expected = try {
            repository.fetchEntitiesFromRemote(ORDER_BY_NONE)
        } catch (e: Exception) {
            e
        }

        // THEN
        Truth.assertThat(expected).isInstanceOf(Exception::class.java)
        coVerify(exactly = 1) { remoteDataSource.getPropertyDTOs() }
        verify(exactly = 0) { mapper.map(propertyDTOList) }
    }

    @Test
    fun `given remote data source return PropertyDTO list, should return PropertyEntity list`() =
        runBlockingTest {

            // GIVEN
            val actual = entityList
            coEvery { remoteDataSource.getPropertyDTOs() } returns propertyDTOList
            every { mapper.map(propertyDTOList) } returns entityList

            // WHEN
            val expected = repository.fetchEntitiesFromRemote()

            // THEN
            Truth.assertThat(expected).isEqualTo(actual)
            coVerifyOrder {
                remoteDataSource.getPropertyDTOs()
                mapper.map(propertyDTOList)
            }
        }

    @Test
    fun `given page 2 returned data returned should have current page number 2 with Pagination`() =
        runBlockingTest {

            // GIVEN

            // Page 1 Pagination
            val page1DTO = propertyDTOListPage1
            val page1Data = entityListPage1

            coEvery {
                remoteDataSource.getPropertyDTOsWithPagination(1)
            } returns page1DTO

            every { mapper.map(page1DTO) } returns page1Data

            // Page 2 Pagination
            val page2DTO = propertyDTOListPage2
            val page2Data = entityListPage2

            coEvery {
                remoteDataSource.getPropertyDTOsWithPagination(2)
            } returns page2DTO

            every { mapper.map(page2DTO) } returns page2Data

            // WHEN
            val expected1 = repository.fetchEntitiesFromRemoteByPage(1)
            val page1 = repository.getCurrentPageNumber()

            val expected2 = repository.fetchEntitiesFromRemoteByPage(2)
            val page2 = repository.getCurrentPageNumber()

            // THEN
            Truth.assertThat(expected1).isEqualTo(page1Data)
            Truth.assertThat(page1).isEqualTo(1)

            Truth.assertThat(expected2).isEqualTo(page2Data)
            Truth.assertThat(page2).isEqualTo(2)

            coVerifyOrder {
                remoteDataSource.getPropertyDTOs()
                mapper.map(propertyDTOList)
            }
        }

    @Test
    fun `given DB is empty should return an empty list`() = runBlockingTest {

        // GIVEN
        val expected = listOf<PropertyEntity>()
        coEvery { localDataSource.getPropertyEntities() } returns expected

        // WHEN
        val actual = repository.getPropertyEntitiesFromLocal()

        // THEN
        Truth.assertThat(actual).isEmpty()
        coVerify(exactly = 1) { localDataSource.getPropertyEntities() }
    }

    @Test
    fun `given DB is populated should return data list`() = runBlockingTest {

        // GIVEN
        coEvery { localDataSource.getPropertyEntities() } returns entityList

        // WHEN
        val actual = repository.getPropertyEntitiesFromLocal()

        // THEN
        Truth.assertThat(actual)
            .containsExactlyElementsIn(entityList)
        coVerify(exactly = 1) { localDataSource.getPropertyEntities() }
    }

    @Test
    fun `given entities, should save entities`() = runBlockingTest {

        // GIVEN
        val idList = entityList.map {
            it.id.toLong()
        }

        coEvery {
            localDataSource.saveEntities(entityList)
        } returns idList

        // WHEN
        repository.savePropertyEntities(entityList)

        // THEN
        coVerify(exactly = 1) { localDataSource.saveEntities(entityList) }
    }

    @Test
    fun `given no error should delete entities`() = runBlockingTest {

        // GIVEN
        coEvery { localDataSource.deletePropertyEntities() } just runs

        // WHEN
        repository.deletePropertyEntities()

        // THEN
        coVerify(exactly = 1) {
            localDataSource.deletePropertyEntities()
        }
    }

    @BeforeEach
    fun setUp() {
        repository = PropertyRepositoryImplCoroutines(localDataSource, remoteDataSource, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(localDataSource, remoteDataSource, mapper)
    }
}
