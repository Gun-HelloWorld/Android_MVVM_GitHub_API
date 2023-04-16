package com.gun.testcodeexample.ui.user.list.state

import com.gun.testcodeexample.data.dto.user.User

sealed class EventState {
    data class MoveDetailActivity(val user: User) : EventState()
}

