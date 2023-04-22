package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.dto.result.ApiResult
import com.gun.githubapi.data.service.RepositoryService
import com.gun.githubapi.data.service.handleApi

class RepositoryRemoteDataSourceImpl(
    private val repositoryService: RepositoryService
) : RepositoryRemoteDataSource {

    override suspend fun fetchRepositoryList(userName: String): ApiResult<List<Repository>> {
        return handleApi {
            repositoryService.fetchRepositoryList(userName)
        }
    }
}
