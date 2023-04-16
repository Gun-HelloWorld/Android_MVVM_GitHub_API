package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.repository.Repository

interface RepositoryRemoteDataSource {
    suspend fun fetchRepositoryList(userName: String): MutableList<Repository>
}