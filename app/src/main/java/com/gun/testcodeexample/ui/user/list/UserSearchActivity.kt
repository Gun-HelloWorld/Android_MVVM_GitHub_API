package com.gun.testcodeexample.ui.user.list

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.google.android.material.snackbar.Snackbar
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.BaseActivity
import com.gun.testcodeexample.common.ErrorMessageParser
import com.gun.testcodeexample.common.recyclerview.ItemClickTransitionListener
import com.gun.testcodeexample.common.state.LoadingState
import com.gun.testcodeexample.data.dto.user.User
import com.gun.testcodeexample.databinding.ActivityUserSearchBinding
import com.gun.testcodeexample.ui.user.detail.UserDetailActivity
import com.gun.testcodeexample.viewmodel.UserViewModel
import com.gun.testcodeexample.viewmodel.UserViewModel.Mode
import com.gun.testcodeexample.viewmodel.UserViewModel.ViewState.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserSearchActivity : BaseActivity(), ItemClickTransitionListener<User> {

    private val sharedElementsMap: MutableMap<String, Pair<View, String>> = mutableMapOf()

    private val userViewModel by viewModels<UserViewModel> { UserViewModel.Factory }

    private val recyclerAdapter = UserSearchRecyclerAdapter(this)

    private lateinit var binding: ActivityUserSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initObserver()
    }

    private fun initLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_search)
        binding.userViewModel = userViewModel

        initLoadingBar(binding.loadingBar)

        binding.recyclerView.apply {
            adapter = recyclerAdapter.withLoadStateFooter(LoadingStateAdapter())
            setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        lifecycleScope.launchWhenStarted {

            launch {
                userViewModel.loadingState.collect {
                    showLoadingBar(it.isShow)
                }
            }

            launch {
                userViewModel.errorState.collect {
                    val message = ErrorMessageParser.parseToErrorMessage(resources, it)
                    Snackbar.make(binding.rootLayout, message, Snackbar.LENGTH_SHORT).show()
                }
            }

            launch {
                userViewModel.viewState.collectLatest {
                    when (it) {
                        is UserViewModel.ViewState.Nothing -> { }

                        is ShowUserList -> {
                            recyclerAdapter.submitData(it.userList)
                        }

                        is ShowUser -> {
                            if (it.mode == Mode.MOVE_DETAIL) {
                                startDetailActivity(it.user)
                                return@collectLatest
                            }

                            recyclerAdapter.submitData(lifecycle, PagingData.empty())
                            recyclerAdapter.submitData(PagingData.from(listOf(it.user)))
                        }

                        is MoveDetailActivity -> {
                            startDetailActivity(it.user)
                        }
                    }
                }
            }
        }
    }

    private fun startDetailActivity(user: User) {
        val pairList = sharedElementsMap[user.login]
        UserDetailActivity.startActivity(this, user, pairList)
    }

    override fun onItemClick(view: View, transitionView: View, data: User) {
        val isLastLifecycleResume = this.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

        // 중복잡업 막기 (화면 전환 시 애니메이션으로 인해 추가적으로 수명주기 체크 로직 포함)
        if (userViewModel.loadingState.value == LoadingState(true) || !isLastLifecycleResume) {
            return
        }

        val imageTransitionPair = Pair(transitionView, transitionView.transitionName)
        sharedElementsMap[data.login] = imageTransitionPair

        userViewModel.onClickItem(data)
    }
}