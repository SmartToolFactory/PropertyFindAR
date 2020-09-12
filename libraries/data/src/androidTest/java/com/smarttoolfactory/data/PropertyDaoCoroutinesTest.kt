package com.smarttoolfactory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.smarttoolfactory.data.db.PropertyDatabase
import com.smarttoolfactory.data.db.dao.PropertyDaoCoroutines
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoEntityListMapper
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.rule.TestCoroutineRule
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PropertyDaoCoroutinesTest {

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

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var database: PropertyDatabase

    /**
     * This is the SUT
     */
    private lateinit var dao: PropertyDaoCoroutines

    /*
        Insert
     */

    @Test
    fun shouldInsertSingleProperty() = runBlockingTest {

        // GIVEN
        val initialCount = dao.getPropertyCount()

        // WHEN
        val insertedId = dao.insert(entityList.first())

        // THEN
        val propertyCount = dao.getPropertyCount()

        Truth.assertThat(initialCount).isEqualTo(0)
        Truth.assertThat(insertedId).isEqualTo(entityList.first().id)
        Truth.assertThat(propertyCount).isEqualTo(1)
    }

    @Test
    fun shouldInsertMultipleProperties() = runBlockingTest {

        // GIVEN
        val initialCount = dao.getPropertyCount()

        // WHEN
        val ids = dao.insert(entityList)

        // THEN
        val propertyCount = dao.getPropertyCount()
        Truth.assertThat(initialCount).isEqualTo(0)
        Truth.assertThat(propertyCount).isEqualTo(entityList.size)
    }

    /*
        Get Property List Suspend
     */

    @Test
    fun givenDBEmptyShouldReturnEmptyList() = runBlockingTest {

        // GIVEN
        val propertyCount = dao.getPropertyCount()

        // WHEN
        val propertyEntityList = dao.getPropertyList()

        // THEN
        Truth.assertThat(propertyCount).isEqualTo(0)
        Truth.assertThat(propertyEntityList).hasSize(0)
    }

    @Test
    fun giveDBPopulatedShouldReturnCorrectList() = runBlockingTest {

        // GIVEN
        val expected = entityList[0]
        dao.insert(expected)

        // WHEN
        val propertyList = dao.getPropertyList()

        // THEN
        val actual = propertyList[0]
        Truth.assertThat(actual).isEqualTo(expected)
    }

    /*
        Delete Suspend
     */

    @Test
    fun givenEveryPropertyDeletedShouldReturnEmptyList() = runBlockingTest {

        // GIVEN
        dao.insert(entityList)
        val initialPropertyCount = dao.getPropertyCount()

        // WHEN
        dao.deleteAll()

        // THEN
        val propertyCount = dao.getPropertyCount()
        Truth.assertThat(initialPropertyCount).isEqualTo(entityList.size)
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

        dao = database.propertyDaoCoroutines()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database.close()
    }
}
