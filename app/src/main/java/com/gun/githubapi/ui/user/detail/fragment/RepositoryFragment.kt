package com.gun.githubapi.ui.user.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gun.githubapi.R
import com.gun.githubapi.common.BaseFragment
import com.gun.githubapi.common.Constants.KEY_USER_NICKNAME
import com.gun.githubapi.databinding.FragmentRepositoryBinding
import com.gun.githubapi.ui.user.detail.RepositoryRecyclerAdapter
import com.gun.githubapi.ui.user.detail.UserDetailViewModel
import kotlinx.coroutines.launch

class RepositoryFragment : BaseFragment() {

    private val userDetailViewModel by viewModels<UserDetailViewModel> { UserDetailViewModel.Factory }

    private lateinit var binding: FragmentRepositoryBinding
    private val recyclerAdapter = RepositoryRecyclerAdapter()

    private val userNickName by lazy { requireArguments().getString(KEY_USER_NICKNAME) }

    companion object {
        fun newInstance(nickName: String) = RepositoryFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_USER_NICKNAME, nickName)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_repository, container, false)

        userNickName?.let {
            userDetailViewModel.setUserNickName(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = recyclerAdapter
            customErrorView.setRetryClickListener(onRetryClickListener)
        }

        initObserver()

        userDetailViewModel.fetchRepositoryList()
    }

    private val onRetryClickListener: OnClickListener = OnClickListener {
        userDetailViewModel.fetchRepositoryList()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    userDetailViewModel.loadingStateFlow.collect {
                        if (it.isShow) {
                            binding.customErrorView.hide()
                        }

                        binding.loadingBar.showLoadingBar(it.isShow)
                    }
                }

                launch {
                    userDetailViewModel.errorSharedFlow.collect {
                        binding.customErrorView.show(it)
                    }
                }

                launch {
                    userDetailViewModel.repoDataStateFlow.collect {
                        recyclerAdapter.submitList(it)
                    }
                }
            }
        }
    }

}