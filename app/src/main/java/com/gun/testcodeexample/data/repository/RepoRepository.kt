package com.gun.testcodeexample.data.repository

import com.gun.testcodeexample.data.dto.repository.Repository

interface RepoRepository {
    suspend fun fetchRepositoryList(userName: String): MutableList<Repository>
}