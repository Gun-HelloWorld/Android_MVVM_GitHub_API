package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.source.UserRemoteDataSource
import com.gun.testcodeexample.data.dto.user.User

class FakeUserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun fetchUserList(): MutableList<User> {
        return userRemoteDataSource.fetchUserList()
    }

    override suspend fun fetchUser(userName: String): User {
        return userRemoteDataSource.fetchUser(userName)
    }
}