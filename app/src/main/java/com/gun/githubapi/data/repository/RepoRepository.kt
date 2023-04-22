package com.gun.githubapi.data.repository

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.dto.result.ApiResult

interface RepoRepository {
    suspend fun fetchRepositoryList(userName: String): ApiResult<List<Repository>>
}