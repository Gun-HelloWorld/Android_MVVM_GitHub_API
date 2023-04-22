package com.gun.githubapi.ui.user.detail

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.githubapi.api.retrofit.RetrofitProvider
import com.gun.githubapi.common.BaseViewModel
import com.gun.githubapi.common.state.LoadingState
import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.data.repository.RepoRepository
import com.gun.githubapi.data.repository.RepoRepositoryImpl
import com.gun.githubapi.data.repository.UserRepository
import com.gun.githubapi.data.repository.UserRepositoryImpl
import com.gun.githubapi.data.dto.result.ApiResult
import com.gun.githubapi.data.dto.error.ErrorData
import com.gun.githubapi.data.source.RepositoryRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemotePagingDataSourceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val repoRepository: RepoRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var _nickname: String? = null

    private val _repoDataStateFlow: MutableStateFlow<List<Repository>> = MutableStateFlow(emptyList())
    val repoDataStateFlow = _repoDataStateFlow.asStateFlow()

    private val _followerDataStateFlow: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val followerDataState = _followerDataStateFlow.asStateFlow()

    private val _followingDataStateFlow: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val followingDataStateFlow = _followingDataStateFlow.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repositoryService = RetrofitProvider.getRepositoryService()
                val userService = RetrofitProvider.getUserService()
                val repositoryDataSource = RepositoryRemoteDataSourceImpl(repositoryService)
                val userDataSource = UserRemoteDataSourceImpl(userService)
                val userPagingDataSource = UserRemotePagingDataSourceImpl(userService)
                val repoRepository = RepoRepositoryImpl(repositoryDataSource)
                val userRepository = UserRepositoryImpl(userDataSource, userPagingDataSource)
                UserDetailViewModel(repoRepository, userRepository)
            }
        }
    }

    fun setUserNickName(nickname: String) {
        _nickname = nickname
    }

    fun fetchRepositoryList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch {
            _nickname?.let {
                when (val response = repoRepository.fetchRepositoryList(it)) {
                    is ApiResult.ApiSuccess -> _repoDataStateFlow.emit(response.data)
                    is ApiResult.ApiError -> _errorSharedFlow.emit(ErrorData(response.code, response.message))
                    is ApiResult.ApiException -> _errorSharedFlow.emit(ErrorData(message = response.e.message))
                }
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }

    fun fetchFollowerList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            _nickname?.let {
                when (val response = userRepository.fetchFollowerList(it)) {
                    is ApiResult.ApiSuccess -> _followerDataStateFlow.emit(response.data)
                    is ApiResult.ApiError -> _errorSharedFlow.emit(ErrorData(response.code, response.message))
                    is ApiResult.ApiException -> _errorSharedFlow.emit(ErrorData(message = response.e.message))
                }
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }

    fun fetchFollowingList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            _nickname?.let {
                when (val response = userRepository.fetchFollowerList(it)) {
                    is ApiResult.ApiSuccess -> _followingDataStateFlow.emit(response.data)
                    is ApiResult.ApiError -> _errorSharedFlow.emit(ErrorData(response.code, response.message))
                    is ApiResult.ApiException -> _errorSharedFlow.emit(ErrorData(message = response.e.message))
                }
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }
}