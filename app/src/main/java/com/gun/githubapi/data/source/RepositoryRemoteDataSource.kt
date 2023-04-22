package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.dto.result.ApiResult

interface RepositoryRemoteDataSource {
    suspend fun fetchRepositoryList(userName: String): ApiResult<List<Repository>>
}