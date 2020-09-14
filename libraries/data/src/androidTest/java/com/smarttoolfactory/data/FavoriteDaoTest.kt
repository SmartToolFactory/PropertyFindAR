package com.smarttoolfactory.data

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.RoomDatabase
import com.google.common.truth.Truth
import com.smarttoolfactory.data.db.dao.FavoritesDao
import com.smarttoolfactory.data.db.dao.UserDao
import com.smarttoolfactory.data.mapper.MapperFactory
import com.smarttoolfactory.data.mapper.PropertyDTOtoFavoriteEntityListMapper
import com.smarttoolfactory.data.model.local.FavoritePropertyEntity
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
 * Test for testing [FavoritePropertyEntity] and favorites with junction table
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

        private val favorite1 = favoritePropertyEntityList[0].apply {
            favorite = true
            displayCount = 5
        }

        private val favorite2 = favoritePropertyEntityList[1].apply {
            favorite = true
            displayCount = 3
        }

        private val favorite3 = favoritePropertyEntityList[2].apply {
            favorite = false
            displayCount = 10
        }

        private val favorite4 = favoritePropertyEntityList[3].apply {
            favorite = false
            displayCount = 2
        }
    }

    @Test
    fun shouldInsertUserFavorite(): Unit = runBlocking {

        // GIVEN
        userDao.insert(user1)

        val users = userDao.getAllUsers()

        val user1Id = users.first().userId

        // WHEN
        val actual1 = favoritesDao.insertUserFavorite(user1Id, favorite1)
        val actual2 = favoritesDao.insertUserFavorite(user1Id, favorite2)

        // THEN
        Truth.assertThat(actual1).isEqualTo(favorite1.id.toLong())
        Truth.assertThat(actual2).isEqualTo(favorite2.id.toLong())
    }

    @Test
    fun shouldInsertUserFavoriteList(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))

        val users = userDao.getAllUsers()

        val user1Id = users.first().userId
        val user2Id = users[1].userId

        // WHEN
        val actual1 = favoritesDao.insertUserFavoritesList(user1Id, listOf(favorite1, favorite2))
        val actual2 = favoritesDao.insertUserFavoritesList(user2Id, listOf(favorite3, favorite4))

        // THEN
        Truth.assertThat(actual1[0]).isEqualTo(favorite1.id.toLong())
        Truth.assertThat(actual1[1]).isEqualTo(favorite2.id.toLong())

        Truth.assertThat(actual2[0]).isEqualTo(favorite3.id.toLong())
        Truth.assertThat(actual2[1]).isEqualTo(favorite4.id.toLong())
    }

    @Test
    fun shouldNotInsertToJunctionAndFavoriteTablesWhenUserWasNotInserted(): Unit = runBlocking {

        // GIVEN

        // WHEN
        val result = runCatching {
            favoritesDao.insertUserFavoritesList(user1.userId, listOf(favorite1, favorite2))
        }

        // THEN
        Truth.assertThat(result.isFailure)
        Truth.assertThat(result.exceptionOrNull())
            .isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun shouldReturnUserFavorites(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavoritesList(user1.userId, listOf(favorite1, favorite2))
        favoritesDao.insertUserFavoritesList(user2.userId, listOf(favorite3, favorite4))

        // WHEN
        val usersWithFavorites = favoritesDao.getUsersAndFavorites()

        // THEN
        Truth.assertThat(usersWithFavorites[0].favorites).containsExactly(favorite1, favorite2)
        Truth.assertThat(usersWithFavorites[1].favorites).containsExactly(favorite3, favorite4)
    }

    @Test
    fun givenDBEmptyShouldReturnEmptyList(): Unit = runBlocking {

        // GIVEN

        // WHEN
        val usersWithFavorites = favoritesDao.getUsersAndFavorites()

        // THEN
        Truth.assertThat(usersWithFavorites).isEmpty()
    }

    @Test
    fun shouldReturnFavoritesForUserWithSpecificId(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavoritesList(user1.userId, listOf(favorite1, favorite2))
        favoritesDao.insertUserFavoritesList(user2.userId, listOf(favorite3, favorite4))

        // WHEN
        val user2WithFavorites = favoritesDao.getUserByIdAndFavorites(user2.userId)

        // THEN
        Truth.assertThat(user2WithFavorites[0].user).isEqualTo(user2)
        Truth.assertThat(user2WithFavorites[0].favorites).containsExactly(favorite3, favorite4)
    }

    @Test
    fun shouldReturnFavoritesWithJoin(): Unit = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        favoritesDao.insertUserFavoritesList(user1.userId, listOf(favorite1, favorite2))
        favoritesDao.insertUserFavoritesList(user2.userId, listOf(favorite3, favorite4))

        // WHEN
        val favoritesList = favoritesDao.getFavoritesOfUser(user2.userId)

        // THEN
        Truth.assertThat(favoritesList).containsExactly(favorite3, favorite4)
    }

    @Before
    override fun setUp() {
        super.setUp()
        favoritesDao = database.favoritesDao()
        userDao = database.userDao()
    }
}
