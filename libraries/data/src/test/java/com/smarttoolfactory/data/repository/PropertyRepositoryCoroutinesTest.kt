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
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Used single instance of tests instead of creating new instance for each test.
 *
 * @see <a href="https://phauer.com/2018/best-practices-unit-testing-kotlin/">Kotlin Test Performance</a>
 *
 */
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
            val slot = slot<String>()

            val actual = entityList
            coEvery { remoteDataSource.getPropertyDTOs() } returns propertyDTOList
            every { mapper.map(propertyDTOList) } returns entityList
            coEvery { localDataSource.saveOrderKey(capture(slot)) } just runs

            // WHEN
            val expected = repository.fetchEntitiesFromRemote()

            // THEN
            Truth.assertThat(expected).isEqualTo(actual)
            Truth.assertThat(slot.captured).isEqualTo(ORDER_BY_NONE)

            coVerifyOrder {
                remoteDataSource.getPropertyDTOs()
                localDataSource.saveOrderKey(ORDER_BY_NONE)
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
