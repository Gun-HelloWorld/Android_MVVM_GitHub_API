package com.gun.githubapi.data.source

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.service.RepositoryService

class RepositoryRemoteDataSourceImpl(
    private val repositoryService: RepositoryService
) : RepositoryRemoteDataSource {

    override suspend fun fetchRepositoryList(userName: String): MutableList<Repository> {
//        val mockResponse: Response<Account> = Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
//        throw HttpException(mockResponse)
        return repositoryService.fetchRepositoryList(userName)
    }

}
