package com.gun.githubapi.ui.user.detail.state

import com.gun.githubapi.data.dto.repository.Repository
import com.gun.githubapi.data.dto.user.User

sealed class RepositoryDataState {
    data class RepositoryListLoadSuccess(val repositoryList: MutableList<Repository>) : RepositoryDataState()
}

sealed class FollowerDataState {
    data class FollowerListLoadSuccess(val followerList: MutableList<User>) : FollowerDataState()
}

sealed class FollowingDataState {
    data class FollowingListLoadSuccess(val followingList: MutableList<User>) : FollowerDataState()
}