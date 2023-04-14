package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.source.UserRemoteDataSource

class FakeUserRemoteDataSourceImpl(private val userList: MutableList<User>): UserRemoteDataSource {
    override suspend fun fetchUserList(): MutableList<User> {
        return userList
    }

    override suspend fun fetchUser(userName: String): User {
        return userList.first { userName.equals(it.login) }
    }
}