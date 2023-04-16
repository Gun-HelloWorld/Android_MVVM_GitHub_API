package com.gun.testcodeexample.ui.user.detail

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.repository.RepoRepository
import com.gun.testcodeexample.data.repository.RepoRepositoryImpl
import com.gun.testcodeexample.data.source.RepositoryRemoteDataSourceImpl
import com.gun.testcodeexample.ui.user.detail.state.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoryViewModel(private val repoRepository: RepoRepository) : BaseViewModel() {

    private val _dataStateFlow = MutableStateFlow(DataState.RepositoryListLoadSuccess(mutableListOf()))
    val dataStateFlow = _dataStateFlow.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repositoryService = RetrofitProvider.getRepositoryService()
                val repositoryDataSource = RepositoryRemoteDataSourceImpl(repositoryService)
                val repoRepository = RepoRepositoryImpl(repositoryDataSource)
                RepositoryViewModel(repoRepository)
            }
        }
    }

    fun fetchRepositoryList(userName: String) {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val repositoryList = repoRepository.fetchRepositoryList(userName)
            _dataStateFlow.value = DataState.RepositoryListLoadSuccess(repositoryList)
            _loadingStateFlow.value = LoadingState(false)
        }
    }
}