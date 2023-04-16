package com.gun.githubapi.ui.user.list.state

import com.gun.githubapi.data.dto.user.User

sealed class EventState {
    data class MoveDetailActivity(val user: User) : EventState()
}

