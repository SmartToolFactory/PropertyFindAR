package com.smarttoolfactory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.smarttoolfactory.data.db.dao.UserDao
import com.smarttoolfactory.data.model.local.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDaoTest : AbstractDaoTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userDao: UserDao

    companion object {

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
    }

    @Test
    fun givenDBEmptyShouldReturnEmptyList() = runBlocking {

        // GIVEN
        val expected = listOf<UserEntity>()

        // WHEN
        val actual = userDao.getAllUsers()

        // THEN
        Truth.assertThat(actual.size).isEqualTo(expected.size)
    }

    @Test
    fun givenDBPopulatedShouldReturnUserList() = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))

        // WHEN
        val users = userDao.getAllUsers()

        // THEN
        Truth.assertThat(users[0]).isEqualTo(user1)
        Truth.assertThat(users[1]).isEqualTo(user2)
    }

    @Test
    fun givenDBEmptyShouldReturnNull() = runBlocking {

        // GIVEN

        // WHEN
        val user = userDao.getUserById(user1.userId)

        // THEN
        Truth.assertThat(user).isNull()
    }

    @Test
    fun givenDBPopulatedShouldReturnUserById() = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))

        // WHEN
        val user = userDao.getUserById(user1.userId)

        // THEN
        Truth.assertThat(user).isEqualTo(user1)
    }

    @Test
    fun givenDBPopulatedShouldDeleteExistingUsers() = runBlocking {

        // GIVEN
        userDao.insert(listOf(user1, user2))
        val initialUserCount = userDao.getAllUsers().size

        // WHEN
        userDao.deleteAll()
        val finalUserCount = userDao.getAllUsers().size

        // THEN
        Truth.assertThat(initialUserCount).isEqualTo(2)
        Truth.assertThat(finalUserCount).isEqualTo(0)
    }

    @Before
    override fun setUp() {
        super.setUp()
        userDao = database.userDao()
    }
}
