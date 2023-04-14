package com.gun.testcodeexample.viewmodel

import com.gun.testcodeexample.common.state.ErrorState
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.data.repository.FakeUserRemoteDataSourceImpl
import com.gun.testcodeexample.data.repository.FakeUserRepositoryImpl
import com.gun.testcodeexample.rule.MainDispatcherRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {
    private lateinit var userViewModel: UserViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val testUserList: MutableList<User> = mutableListOf()

    @Before
    fun setUp() {
        testUserList.addAll(
            arrayListOf(
                User(id = 1, login = "User1"),
                User(id = 2, login = "User2"),
                User(id = 3, login = "User3")
            )
        )
        val fakeUserRemoteDataSourceImpl = FakeUserRemoteDataSourceImpl(testUserList)
        val userRepository = FakeUserRepositoryImpl(fakeUserRemoteDataSourceImpl)
        userViewModel = UserViewModel(userRepository)
    }

    @Test
    fun fetchUserList_success_call() {
        val viewModel: UserViewModel = mockk()

        every { viewModel.fetchUserList() } just Runs

        viewModel.fetchUserList()

        verify { viewModel.fetchUserList() }
    }

    @Test
    fun fetchUserList_success_viewState_change() = runTest {
        val viewStateList = mutableListOf<UserViewModel.ViewState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            launch {
                userViewModel.viewState
                    .take(2)
                    .drop(1) // Drop default value
                    .toList(viewStateList)
            }

            launch {
                userViewModel.fetchUserList()
            }
        }

        advanceUntilIdle()

        val userList = (viewStateList[0] as UserViewModel.ViewState.ShowUserList).userList

        // ViewState Assert
        assertEquals(true, viewStateList.isNotEmpty())
        assertEquals(1, viewStateList.size)
        assertEquals(true, testUserList.containsAll(userList))

        job.cancel()
    }

    @Test
    fun fetchUserList_success_loadingState_change() = runTest {
        val loadingStateList = mutableListOf<LoadingState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            launch {
                userViewModel.loadingState
                    .take(3)
                    .drop(1) // Drop default value
                    .toList(loadingStateList)
            }

            launch {
                userViewModel.fetchUserList()
            }
        }

        advanceUntilIdle()

        // LoadingState Assert
        assertEquals(loadingStateList.size, 2)
        assertEquals(true, loadingStateList[0].isShow)
        assertEquals(false, loadingStateList[1].isShow)

        job.cancel()
    }

    @Test
    fun fetchUserList_success_errorState_change() = runTest {
        val errorStateList = mutableListOf<ErrorState>()

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            launch {
                userViewModel.errorState.take(1).toList(errorStateList)
            }

            launch {
                userViewModel.fetchUserList()
            }
        }

        advanceUntilIdle()

        // ErrorState Assert
        assertEquals(errorStateList.size, 0)

        job.cancel()
    }
}