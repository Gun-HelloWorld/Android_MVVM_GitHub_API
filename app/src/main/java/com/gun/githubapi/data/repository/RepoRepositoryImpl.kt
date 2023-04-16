package com.gun.githubapi.data.repository

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.source.RepositoryRemoteDataSource

class RepoRepositoryImpl(
    private val repositoryRemoteDataSource: RepositoryRemoteDataSource
) : RepoRepository {

    override suspend fun fetchRepositoryList(userName: String): MutableList<Repository> {
        return repositoryRemoteDataSource.fetchRepositoryList(userName)
    }

}