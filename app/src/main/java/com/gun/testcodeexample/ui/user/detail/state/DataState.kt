package com.gun.testcodeexample.ui.user.detail.state

import com.gun.testcodeexample.data.dto.repository.Repository

sealed class DataState {
    data class RepositoryListLoadSuccess(val repositoryList: MutableList<Repository>) : DataState()
}