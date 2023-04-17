package com.gun.githubapi.ui.user.detail

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gun.githubapi.api.retrofit.RetrofitProvider
import com.gun.githubapi.common.BaseViewModel
import com.gun.githubapi.common.state.LoadingState
import com.gun.githubapi.data.repository.RepoRepository
import com.gun.githubapi.data.repository.RepoRepositoryImpl
import com.gun.githubapi.data.repository.UserRepository
import com.gun.githubapi.data.repository.UserRepositoryImpl
import com.gun.githubapi.data.source.RepositoryRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemoteDataSourceImpl
import com.gun.githubapi.data.source.UserRemotePagingDataSourceImpl
import com.gun.githubapi.ui.user.detail.state.FollowerDataState
import com.gun.githubapi.ui.user.detail.state.FollowingDataState
import com.gun.githubapi.ui.user.detail.state.RepositoryDataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val repoRepository: RepoRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    private var _nickname: String? = null

    private val _selectedPageStateFlow =
        MutableStateFlow(0)
    val selectedPageStateFlow = _selectedPageStateFlow.asStateFlow()

    private val _repoDataStateFlow =
        MutableStateFlow(RepositoryDataState.RepositoryListLoadSuccess(mutableListOf()))
    val repoDataStateFlow = _repoDataStateFlow.asStateFlow()

    private val _followerDataStateFlow =
        MutableStateFlow(FollowerDataState.FollowerListLoadSuccess(mutableListOf()))
    val followerDataState = _followerDataStateFlow.asStateFlow()

    private val _followingDataStateFlow =
        MutableStateFlow(FollowingDataState.FollowingListLoadSuccess(mutableListOf()))
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

    fun setSelectedPageStateFlow(tabPosition: Int) {
        _selectedPageStateFlow.value = tabPosition
    }

    fun fetchRepositoryList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            _nickname?.let {
                val repositoryList = repoRepository.fetchRepositoryList(it)
                _repoDataStateFlow.value = RepositoryDataState.RepositoryListLoadSuccess(repositoryList)
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }

    fun fetchFollowerList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            _nickname?.let {
                val followerList = userRepository.fetchFollowerList(it)
                _followerDataStateFlow.value = FollowerDataState.FollowerListLoadSuccess(followerList)
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }

    fun fetchFollowingList() {
        _loadingStateFlow.value = LoadingState(true)

        viewModelScope.launch(exceptionHandler) {
            _nickname?.let {
                val followingList = userRepository.fetchFollowingList(it)
                _followingDataStateFlow.value = FollowingDataState.FollowingListLoadSuccess(followingList)
                _loadingStateFlow.value = LoadingState(false)
            }
        }
    }

    fun onClickTabMenu(tabPosition: Int) {
        setSelectedPageStateFlow(tabPosition)
    }
}