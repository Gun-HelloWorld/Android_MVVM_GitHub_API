package com.gun.githubapi.ui.user.list

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.gun.githubapi.R
import com.gun.githubapi.common.BaseActivity
import com.gun.githubapi.common.ext.getErrorData
import com.gun.githubapi.common.ext.getErrorState
import com.gun.githubapi.common.recyclerview.ItemClickTransitionListener
import com.gun.githubapi.common.state.LoadingState
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.databinding.ActivityUserSearchBinding
import com.gun.githubapi.ui.user.detail.UserDetailActivity
import com.gun.githubapi.ui.user.list.state.DataState
import com.gun.githubapi.ui.user.list.state.EventState
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

        recyclerAdapter.addLoadStateListener {
            it.getErrorState()?.let { error ->
                userViewModel.dataStateChange(DataState.Clear)
                binding.customErrorView.show(error.getErrorData())
            }

            val isLoading = it.source.refresh is LoadState.Loading
            userViewModel.loadingStateChange(isLoading)
        }

        with(binding) {
            lifecycleOwner = this@UserSearchActivity
            userViewModel = this@UserSearchActivity.userViewModel
            customErrorView.setRetryClickListener(onRetryClickListener)
            recyclerView.adapter = recyclerAdapter.withLoadStateFooter(LoadingStateAdapter())
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.loadingStateFlow.collect {
                        if (it.isShow) {
                            binding.customErrorView.hide()
                        }

                        binding.loadingBar.showLoadingBar(it.isShow)
                    }
                }

                launch {
                    userViewModel.errorSharedFlow.collect {
                        binding.customErrorView.show(it)
                        recyclerAdapter.submitData(PagingData.empty())
                    }
                }

                launch {
                    userViewModel.dataStateFlow.collectLatest {
                        when (it) {
                            is DataState.Nothing -> {}

                            is DataState.Clear -> {
                                recyclerAdapter.submitData(PagingData.empty())
                            }

                            is DataState.ShowUserList -> {
                                recyclerAdapter.submitData(it.userList)
                            }

                            is DataState.ShowUser -> {
                                recyclerAdapter.submitData(PagingData.from(listOf(it.user)))
                            }
                        }
                    }
                }

                launch {
                    userViewModel.eventSharedFlow.collectLatest {
                        when (it) {
                            is EventState.MoveDetailActivity -> {
                                startDetailActivity(it.user)
                            }
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

    private val onRetryClickListener: OnClickListener = OnClickListener {
        userViewModel.retryApiRequest()
    }

    override fun onItemClick(view: View, transitionView: View, data: User) {
        val isLastLifecycleResume = this.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

        // 중복잡업 막기 (화면 전환 시 애니메이션으로 인해 추가적으로 수명주기 체크 로직 포함)
        if (userViewModel.loadingStateFlow.value == LoadingState(true) || !isLastLifecycleResume) {
            return
        }

        val imageTransitionPair = Pair(transitionView, transitionView.transitionName)
        sharedElementsMap[data.login] = imageTransitionPair

        userViewModel.onClickItem(data)
    }
}