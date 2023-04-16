package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.user.User

interface UserRemoteDataSource {
    suspend fun fetchUser(userName: String): User
}