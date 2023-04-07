package com.gun.testcodeexample.data.source

import com.gun.testcodeexample.data.dto.repository.Repository

interface RepositoryRemoteDataSource {
    suspend fun getRepositoryList(userName: String): MutableList<Repository>
}