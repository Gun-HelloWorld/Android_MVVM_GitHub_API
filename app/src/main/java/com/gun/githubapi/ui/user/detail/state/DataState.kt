package com.gun.githubapi.ui.user.detail.state

import com.gun.githubapi.data.dto.repository.Repository

sealed class DataState {
    data class RepositoryListLoadSuccess(val repositoryList: MutableList<Repository>) : DataState()
}