package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.source.UserRemoteDataSource
import com.gun.testcodeexample.data.user.User

class UserRepositoryImpl(private val userRemoteDataSource: UserRemoteDataSource) : UserRepository {
    override suspend fun fetchUserList(): MutableList<User> {
        return userRemoteDataSource.getUserList()
    }

    override suspend fun fetchUser(userName: String): User {
        return userRemoteDataSource.getUser(userName)
    }
}