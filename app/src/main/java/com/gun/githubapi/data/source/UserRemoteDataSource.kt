package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.user.User

interface UserRemoteDataSource {
    suspend fun fetchUser(userName: String): User
    suspend fun fetchFollowerList(userName: String):MutableList<User>
    suspend fun fetchFollowingList(userName: String):MutableList<User>
}