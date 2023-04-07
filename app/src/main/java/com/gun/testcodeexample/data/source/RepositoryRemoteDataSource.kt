package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.dto.repository.Repository

interface RepositoryRemoteDataSource {
    suspend fun fetchRepositoryList(userName: String): MutableList<Repository>
}