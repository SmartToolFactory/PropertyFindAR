package com.smarttoolfactory.data.repository

import com.google.common.truth.Truth
import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.data.source.LocalPropertyDataSourceRxJava3
import com.smarttoolfactory.data.source.RemotePropertyDataSourceRxJava3
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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
        val slot = slot<String>()

        every { remoteDataSource.getPropertyDTOs() } returns Single.just(propertyDTOList)
        every { mapper.map(propertyDTOList) } returns entityList
        every { localDataSource.saveOrderKey(capture(slot)) } returns Completable.complete()

        // WHEN
        val expected = repository.fetchEntitiesFromRemote().blockingGet()

        // THEN
        Truth.assertThat(expected).containsExactlyElementsIn(entityList)
        Truth.assertThat(slot.captured).isEqualTo(ORDER_BY_NONE)

        verifyOrder {
            remoteDataSource.getPropertyDTOs()
            localDataSource.saveOrderKey(ORDER_BY_NONE)
            mapper.map(propertyDTOList)
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
