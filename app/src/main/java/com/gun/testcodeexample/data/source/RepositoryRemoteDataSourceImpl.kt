package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.dto.repository.Repository
import com.gun.testcodeexample.data.service.RepositoryService

class RepositoryRemoteDataSourceImpl(
    private val repositoryService: RepositoryService
) : RepositoryRemoteDataSource {

    override suspend fun getRepositoryList(userName: String): MutableList<Repository> {
//        val mockResponse: Response<Account> = Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
//        throw HttpException(mockResponse)
        return repositoryService.getUserRepository(userName)
    }

}
