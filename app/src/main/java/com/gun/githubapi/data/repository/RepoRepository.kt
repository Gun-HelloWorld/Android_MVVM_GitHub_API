package com.gun.githubapi.data.repository

import com.gun.githubapi.data.dto.repository.Repository

interface RepoRepository {
    suspend fun fetchRepositoryList(userName: String): MutableList<Repository>
}