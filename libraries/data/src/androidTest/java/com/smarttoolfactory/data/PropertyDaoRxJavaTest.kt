package com.smarttoolfactory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.smarttoolfactory.data.db.PropertyDatabase
import com.smarttoolfactory.data.db.dao.PropertyRxJava3Dao
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.local.PropertyEntity
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyDaoRxJavaTest {

    companion object {

        private val propertyDTOList = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!.res

        val entityList =
            MapperFactory.createListMapper<PropertyDTOtoEntityListMapper>()
                .map(propertyDTOList)
    }
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PropertyDatabase

    /**
     * This is the SUT
     */
    private lateinit var dao: PropertyRxJava3Dao

    @Test
    fun shouldInsertSingleProperty() {

        // GIVEN
        val initialCount = dao.getPropertyCount().blockingGet()

        // WHEN
        val testObserver = dao.insert(entityList.first()).test()

        // THEN
        testObserver.assertComplete()
            .assertNoErrors()

        val propertyCount = dao.getPropertyCount().blockingGet()
        Truth.assertThat(initialCount).isEqualTo(0)
        Truth.assertThat(propertyCount).isEqualTo(1)

        testObserver.dispose()
    }

    @Test
    fun shouldInsertMultipleProperties() {

        // GIVEN
        val initialCount = dao.getPropertyCount().blockingGet()

        // WHEN
        val testObserver = dao.insert(entityList).test()

        // THEN
        testObserver.assertComplete()
            .assertNoErrors()

        val propertyCount = dao.getPropertyCount().blockingGet()
        Truth.assertThat(initialCount).isEqualTo(0)
        Truth.assertThat(propertyCount).isEqualTo(entityList.size)

        testObserver.dispose()
    }

    /*
        Get Property List Single
     */
    @Test
    fun givenDBEmptyShouldReturnEmptyListWithSingle() {

        // GIVEN
        val propertyCount = dao.getPropertyCount().blockingGet()

        // WHEN
        val propertyEntityList = dao.getPropertiesSingle().blockingGet()

        // THEN
        Truth.assertThat(propertyCount).isEqualTo(0)
        Truth.assertThat(propertyEntityList).hasSize(0)
    }

    @Test
    fun giveDBPopulatedShouldReturnCorrectListWithSingle() {

        // GIVEN
        val expected = entityList[0]
        dao.insert(expected).blockingAwait()

        // WHEN
        val propertyEntityList = dao.getPropertiesSingle().blockingGet()

        // THEN
        val actual = propertyEntityList[0]
        Truth.assertThat(actual).isEqualTo(expected)
    }

    /*
        Get Property List Maybe
     */
    @Test
    fun givenDBEmptyShouldReturnEmptyListWithMaybe() {

        // GIVEN
        val expected = listOf<PropertyEntity>()

        // WHEN
        val testObserver = dao.getPropertiesMaybe().test()

        // THEN
        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.size == expected.size
            }
            .dispose()
    }

    @Test
    fun giveDBPopulatedShouldReturnCorrectListWithMaybe() {

        // GIVEN
        val expected = entityList[0]
        dao.insert(expected).blockingAwait()

        // WHEN
        val testObserver = dao.getPropertiesMaybe().test()

        // THEN
        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue {
                it.size == 1
            }
            .dispose()
    }

    /*
        Get Property List Observable
     */

    @Test
    fun givenDBEmptyShouldReturnEmptyListWithObservable() {

        // GIVEN
        val expected = listOf<PropertyEntity>()

        // WHEN
        val properties = dao.getProperties().blockingFirst()

        // THEN
        Truth.assertThat(properties.size).isEqualTo(expected.size)
    }

    @Test
    fun giveDBPopulatedShouldReturnCorrectListWithObservable() {

        // GIVEN
        val expected = entityList
        dao.insert(expected).blockingAwait()

        // WHEN
        val properties = dao.getProperties().blockingFirst()

        // THEN
        Truth.assertThat(properties.size).isEqualTo(entityList.size)
        Truth.assertThat(properties).containsExactlyElementsIn(entityList)
    }

    /*
        Delete Suspend
     */
    @Test
    fun givenEveryPropertyDeletedShouldReturnEmptyList() {

        // GIVEN
        dao.insert(entityList).blockingAwait()
        val initialPropertCount = dao.getPropertyCount().blockingGet()

        // WHEN
        dao.deleteAll().blockingAwait()

        // THEN
        val propertyCount = dao.getPropertyCount().blockingGet()
        Truth.assertThat(initialPropertCount).isEqualTo(entityList.size)
        Truth.assertThat(propertyCount).isEqualTo(0)
    }

    @Before
    fun setUp() {

        // using an in-memory database because the information stored here disappears after test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), PropertyDatabase::class.java
        )
            // allowing main thread queries, just for testing
//            .allowMainThreadQueries()
            .build()

        dao = database.propertyDaoRxJava()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database.close()
    }
}
