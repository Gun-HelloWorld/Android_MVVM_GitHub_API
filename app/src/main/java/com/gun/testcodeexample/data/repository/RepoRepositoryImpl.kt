package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.dto.repository.Repository
import com.gun.testcodeexample.data.source.RepositoryRemoteDataSource

class RepoRepositoryImpl(
    private val repositoryRemoteDataSource: RepositoryRemoteDataSource
) : RepoRepository {

    override suspend fun fetchRepositoryList(userName: String): MutableList<Repository> {
        return repositoryRemoteDataSource.getRepositoryList(userName)
    }

}