package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.user.User

interface UserRepository {
    suspend fun fetchUserList(): MutableList<User>
    suspend fun fetchUser(userName: String): User
}