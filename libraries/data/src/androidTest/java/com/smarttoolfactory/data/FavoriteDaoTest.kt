package com.smarttoolfactory.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.RoomDatabase
import com.google.common.truth.Truth
import com.smarttoolfactory.data.db.dao.FavoritesDao
import com.smarttoolfactory.data.db.dao.UserDao
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoFavoriteEntityListMapper
import com.smarttoolfactory.data.model.local.InteractivePropertyEntity
import com.smarttoolfactory.data.model.local.UserEntity
import com.smarttoolfactory.data.model.local.UserFavoriteJunction
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import java.util.concurrent.Executors
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Test for testing [InteractivePropertyEntity] and favorites with junction table
 * [UserFavoriteJunction] table
 *
 * ### IMPORTANT Don't use blocking test with `@Transaction` it causes test to get stuck.
 * Also set [RoomDatabase.Builder.setTransactionExecutor] to [Executors.newSingleThreadExecutor]
 *
 * @see <a href="https://stackoverflow.com/a/57900487/5457853">
 *     Tests stuck when using Room @Transaction function</a>

 */
class FavoriteDaoTest : AbstractDaoTest(inMemoryDatabase = true) {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favoritesDao: FavoritesDao
    private lateinit var userDao: UserDao

    companion object {

        private val propertyDTOs = convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!.res

        val favoritePropertyEntityList =
            MapperFactory.createListMapper<PropertyDTOtoFavoriteEntityListMapper>()
                .map(propertyDTOs)

        // GIVEN
        private val user1 = UserEntity(
            1,
            "Marlon",
            "Brando",
            "email1",
            "password1"
        )
        private val user2 = UserEntity(
            2,
            "Al",
            "Pacino",
            "email2",
            "password2"
        )

        private val favorite1 = favoritePropertyEntityList[0]

        private val favorite2 = favoritePropertyEntityList[1]

        private val favorite3 = favoritePropertyEntityList[2]

        private val favorite4 = favoritePropertyEntityList[3]
    }

    @Test
    fun shouldInsertUserFavorites(): Unit = runBlocking {

        // GIVEN
        userDao.insert(user1)

        val users = userDao.getAllUsers()

        val user1Id = users.first().userId

        // WHEN
        val actual1 = favoritesDao.insertUserFavorite(user1Id, favorite1)
        val actual2 = favoritesDao.insertUserFavorite(user1Id, favorite2)

        // THEN
        val properties = favoritesDao.getInteractiveProperties()

        Truth.assertThat(actual1).isEqualTo(favorite1.id.toLong())
        Truth.assertThat(actual2).isEqualTo(favorite2.id.toLong())
        Truth.assertThat(properties.size).isEqualTo(2)
    }

    @Test
    fun shouldNOTInsertToJunctionAndFavoriteTablesWhenUserWasNotInserted(): Unit = runBlocking {

        // GIVEN

        // WHEN
        val result = runCatching {
            favoritesDao.insertUserFavorite(user1.userId, favorite1)
        }

        // THEN
        val properties = favoritesDao.getInteractiveProperties()

        Truth.assertThat(properties.isEmpty())
        Truth.assertThat(result.isFailure)
        Truth.assertThat(result.exceptionOrNull())
            .isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun shouldReturnUserFavorites(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavorite(user1.userId, favorite1)
        favoritesDao.insertUserFavorite(user1.userId, favorite2)
        favoritesDao.insertUserFavorite(user2.userId, favorite3)
        favoritesDao.insertUserFavorite(user2.userId, favorite4)

        // WHEN
        val usersWithFavorites = favoritesDao.getUsersWithProperties()

        // THEN
        Truth.assertThat(usersWithFavorites[0].properties).containsExactly(favorite1, favorite2)
        Truth.assertThat(usersWithFavorites[1].properties).containsExactly(favorite3, favorite4)
    }

    @Test
    fun givenDBEmptyShouldReturnEmptyList(): Unit = runBlocking {

        // GIVEN
        val users = userDao.getAllUsers()

        // WHEN
        val usersWithProperties = favoritesDao.getUsersWithProperties()

        // THEN
        Truth.assertThat(users.isEmpty())
        Truth.assertThat(usersWithProperties).isEmpty()
    }

    @Test
    fun shouldReturnFavoritesForUserWithSpecificId(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavorite(user1.userId, favorite1)
        favoritesDao.insertUserFavorite(user1.userId, favorite2)
        favoritesDao.insertUserFavorite(user2.userId, favorite3)
        favoritesDao.insertUserFavorite(user2.userId, favorite4)

        // WHEN
        val userWithProperties = favoritesDao.getUserWithProperties(user2.userId)

        // THEN
        Truth.assertThat(userWithProperties?.user).isEqualTo(user2)
        Truth.assertThat(userWithProperties?.properties).containsExactly(favorite3, favorite4)
    }

    @Test
    fun shouldReturnFavoritesWithJoin(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavorite(user1.userId, favorite1, 3, true)
        favoritesDao.insertUserFavorite(user1.userId, favorite2, 5, true)

        // WHEN
        val user1Favorite1Status =
            favoritesDao.getUserFavoriteJunction(user1.userId, favorite1.id)

        val user1Favorite2Status =
            favoritesDao.getUserFavoriteJunction(user1.userId, favorite2.id)

        // THEN
        Truth.assertThat(user1Favorite1Status?.viewCount).isEqualTo(3)
        Truth.assertThat(user1Favorite2Status?.viewCount).isEqualTo(5)
    }

    @Test
    fun shouldModifyFavoriteItemBelongToSpecificUser(): Unit = runBlocking {
        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavorite(user1.userId, favorite1, 5, false)
        favoritesDao.insertUserFavorite(user1.userId, favorite1, 5, false)

        // WHEN
        favoritesDao.insertUserFavorite(user1.userId, favorite2, 100, false)

        // THEN
        val user1Favorite1Status =
            favoritesDao.getUserFavoriteJunction(user1.userId, favorite1.id)

        val user1Favorite2Status =
            favoritesDao.getUserFavoriteJunction(user1.userId, favorite2.id)

        // THEN
        Truth.assertThat(user1Favorite1Status?.viewCount).isEqualTo(5)
        Truth.assertThat(user1Favorite2Status?.viewCount).isEqualTo(100)
    }

    @Test
    fun shouldDeleteFavoriteBelongToSpecificUser(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavorite(user1.userId, favorite1, 1, true)
        favoritesDao.insertUserFavorite(user1.userId, favorite2, 2, true)
        favoritesDao.insertUserFavorite(user1.userId, favorite3, 3, true)

        favoritesDao.insertUserFavorite(user2.userId, favorite1, 1, false)
        favoritesDao.insertUserFavorite(user2.userId, favorite3, 3, false)
        favoritesDao.insertUserFavorite(user2.userId, favorite4, 4, false)

        // WHEN
        favoritesDao.deleteFavoritesForUserWithId(user1.userId, favorite2.id)

        // THEN
        val favorites1 = favoritesDao.getPropertiesWithFavorites(user1.userId)
        val favorites2 = favoritesDao.getPropertiesWithFavorites(user2.userId)

        Truth.assertThat(favorites1.size).isEqualTo(2)
        Truth.assertThat(favorites2.size).isEqualTo(3)
    }

    @Before
    override fun setUp() {
        super.setUp()
        favoritesDao = database.favoritesDao()
        userDao = database.userDao()
    }
}
