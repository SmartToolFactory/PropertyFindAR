package com.smarttoolfactory.data.source

import com.google.common.truth.Truth
import com.smarttoolfactory.data.api.PropertyApiCoroutines
import com.smarttoolfactory.data.db.dao.PropertyCoroutinesDao
import com.smarttoolfactory.data.db.dao.SortOrderDaoCoroutines
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
class PropertyDataSourceCoroutinesTest {

    companion object {

        private val propertyDTOList = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!.res

        val entityList =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(propertyDTOList)
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class RemoteDataSourceTest {

        private val api = mockk<PropertyApiCoroutines>()

        private lateinit var remoteDataSource: RemotePropertyDataSourceCoroutines

        @Test
        fun `given network error occurred, should throw Exception`() = runBlockingTest {

            // GIVEN
            coEvery { api.getPropertyResponse() } throws Exception("Network Exception")

            // WHEN
            val expected = try {
                remoteDataSource.getPropertyDTOs()
            } catch (e: Exception) {
                e
            }

            // THEN
            Truth.assertThat(expected).isInstanceOf(Exception::class.java)
            coVerify(exactly = 1) { api.getPropertyResponse() }
        }

        @Test
        fun `given Http 200, should return DTO list`() = runBlockingTest {

            // GIVEN
            val actual = propertyDTOList
            coEvery { api.getPropertyResponse().res } returns actual

            // WHEN
            val expected = remoteDataSource.getPropertyDTOs()

            // THEN
            Truth.assertThat(expected).isEqualTo(actual)
            coVerify(exactly = 1) { api.getPropertyResponse() }
        }

        @BeforeEach
        fun setUp() {
            remoteDataSource = RemotePropertyDataSourceCoroutinesImpl(api)
        }

        @AfterEach
        fun tearDown() {
            clearMocks(api)
        }
    }

    @Nested
    inner class LocalDataSourceTest {

        private val dao = mockk<PropertyCoroutinesDao>()

        private lateinit var localDataSource: LocalPropertyDataSourceCoroutines
        private val sortDao = mockk<SortOrderDaoCoroutines>()

        @Test
        fun `given DB is empty should return an empty list`() = runBlockingTest {

            // GIVEN
            val expected = listOf<PropertyEntity>()
            coEvery { dao.getPropertyList() } returns expected

            // WHEN
            val actual = localDataSource.getPropertyEntities()

            // THEN
            Truth.assertThat(actual).isEmpty()
            coVerify(exactly = 1) { dao.getPropertyList() }
        }

        @Test
        fun `given DB is populated should return data list`() = runBlockingTest {

            // GIVEN
            coEvery { dao.getPropertyList() } returns entityList

            // WHEN
            val actual = localDataSource.getPropertyEntities()

            // THEN
            Truth.assertThat(actual).containsExactlyElementsIn(entityList)
            coVerify(exactly = 1) { dao.getPropertyList() }
        }

        @Test
        fun `given entities, should save entities to DB`() = runBlockingTest {

            // GIVEN
            val idList = entityList.map {
                it.id.toLong()
            }

            coEvery { dao.insert(entityList) } returns idList

            // WHEN
            val result = localDataSource.saveEntities(entityList)

            // THEN
            Truth.assertThat(result).containsExactlyElementsIn(idList)
            coVerify(exactly = 1) { dao.insert(entityList) }
        }

        @Test
        fun `given no error should delete entities`() = runBlockingTest {

            // GIVEN
            coEvery { dao.deleteAll() } just runs

            // WHEN
            localDataSource.deletePropertyEntities()

            // THEN
            coVerify(exactly = 1) { dao.deleteAll() }
        }

        @BeforeEach
        fun setUp() {
            localDataSource = LocalPropertyDataSourceImpl(dao, sortDao)
        }

        @AfterEach
        fun tearDown() {
            clearMocks(dao, sortDao)
        }
    }
}
