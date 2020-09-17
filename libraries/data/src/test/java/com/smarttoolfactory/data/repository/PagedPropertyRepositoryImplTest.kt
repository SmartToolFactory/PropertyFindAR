package com.smarttoolfactory.data.repository

import com.google.common.truth.Truth
import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoPagedEntityListMapper
import com.smarttoolfactory.data.model.local.PagedPropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.data.source.LocalPagedPropertyDataSource
import com.smarttoolfactory.data.source.RemotePropertyDataSource
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
import io.mockk.slot
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PagedPropertyRepositoryImplTest {

    private lateinit var repository: PagedPropertyRepository

    private val localDataSource: LocalPagedPropertyDataSource = mockk()
    private val remoteDataSource: RemotePropertyDataSource = mockk()
    private val mapper: PropertyDTOtoPagedEntityListMapper = mockk()

    companion object {

        private val propertyResponse = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!

        private val propertyDTOList = propertyResponse.res

        private val entityList =
            MapperFactory.createListMapper<PropertyDTOtoPagedEntityListMapper>()
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
            MapperFactory.createListMapper<PropertyDTOtoPagedEntityListMapper>()
                .map(
                    convertToObjectFromJson<PropertyResponse>(
                        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
                    )!!.res
                )

        private val entityListPage2 =
            MapperFactory.createListMapper<PropertyDTOtoPagedEntityListMapper>()
                .map(
                    convertToObjectFromJson<PropertyResponse>(
                        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
                    )!!.res
                )
    }

    @Test
    fun `given page 2 returned data returned should have current page number 2 with Pagination`() =
        runBlockingTest {

            // GIVEN
            val slot = slot<String>()

            // Page 1 Pagination
            val page1DTO = propertyDTOListPage1
            val page1Data = entityListPage1

            coEvery {
                remoteDataSource.getPropertyDTOsWithPagination(1)
            } returns page1DTO

            every { mapper.map(page1DTO) } returns page1Data
            coEvery { localDataSource.saveOrderKey(capture(slot)) } just runs

            // Page 2 Pagination
            val page2DTO = propertyDTOListPage2
            val page2Data = entityListPage2

            coEvery {
                remoteDataSource.getPropertyDTOsWithPagination(2)
            } returns page2DTO

            every { mapper.map(page2DTO) } returns page2Data

            // WHEN
            val page1 = repository.getCurrentPageNumber()
            val expected1 = repository.fetchEntitiesFromRemoteByPage()

            val page2 = repository.getCurrentPageNumber()
            val expected2 = repository.fetchEntitiesFromRemoteByPage()

            // THEN
            Truth.assertThat(expected1).isEqualTo(page1Data)
            Truth.assertThat(page1).isEqualTo(1)

            Truth.assertThat(expected2).isEqualTo(page2Data)
            Truth.assertThat(page2).isEqualTo(2)

            coVerifyOrder {
                remoteDataSource.getPropertyDTOsWithPagination(1)
                localDataSource.saveOrderKey(ORDER_BY_NONE)
                mapper.map(page1DTO)
                remoteDataSource.getPropertyDTOsWithPagination(2)
                localDataSource.saveOrderKey(ORDER_BY_NONE)
                mapper.map(page2DTO)
            }
        }

    @Test
    fun `given DB is empty should return an empty list`() = runBlockingTest {

        // GIVEN
        val expected = listOf<PagedPropertyEntity>()
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
        repository = PagedPropertyRepositoryImpl(localDataSource, remoteDataSource, mapper)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(localDataSource, remoteDataSource, mapper)
    }
}
