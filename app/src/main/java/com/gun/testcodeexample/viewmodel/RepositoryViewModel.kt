package com.gun.testcodeexample.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.Constants
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.dto.repository.Repository
import com.gun.testcodeexample.data.repository.RepoRepository
import com.gun.testcodeexample.data.repository.RepoRepositoryImpl
import com.gun.testcodeexample.data.source.RepositoryRemoteDataSourceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepositoryViewModel(private val repoRepository: RepoRepository) : BaseViewModel() {

    private val _viewState = MutableStateFlow(ViewState.RepositoryListLoadSuccess(mutableListOf()))
    val viewState = _viewState.asStateFlow()

    sealed class ViewState {
        data class RepositoryListLoadSuccess(val repositoryList: MutableList<Repository>) : ViewState()
    }

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
        _loadingState.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            val repositoryList = repoRepository.fetchRepositoryList(userName)
            Log.d(
                Constants.TAG,
                "RepositoryViewModel - fetchRepositoryList() - repositoryList : $repositoryList"
            )
            _viewState.value = ViewState.RepositoryListLoadSuccess(repositoryList)
            _loadingState.value = LoadingState(false)
        }
    }
}