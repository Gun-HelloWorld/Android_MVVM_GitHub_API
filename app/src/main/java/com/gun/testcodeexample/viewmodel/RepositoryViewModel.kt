package com.gun.testcodeexample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.testcodeexample.api.retrofit.RetrofitProvider
import com.gun.testcodeexample.common.BaseViewModel
import com.gun.testcodeexample.common.Constants
import com.gun.testcodeexample.data.dto.repository.Repository
import com.gun.testcodeexample.data.repository.RepoRepository
import com.gun.testcodeexample.data.repository.RepoRepositoryImpl
import com.gun.testcodeexample.data.source.RepositoryRemoteDataSourceImpl
import kotlinx.coroutines.launch

class RepositoryViewModel(private val repoRepository: RepoRepository) : BaseViewModel() {

    val viewState = MutableLiveData<ViewState>()

    sealed class ViewState {
        data class Loading(val isShow: Boolean) : ViewState()
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
        viewState.value = ViewState.Loading(true)

        viewModelScope.launch(exceptionHandler) {
            val repositoryList = repoRepository.fetchRepositoryList(userName)
            Log.d(
                Constants.TAG,
                "RepositoryViewModel - fetchRepositoryList() - repositoryList : $repositoryList"
            )
            viewState.value = ViewState.RepositoryListLoadSuccess(repositoryList)
            viewState.value = ViewState.Loading(false)
        }
    }
}