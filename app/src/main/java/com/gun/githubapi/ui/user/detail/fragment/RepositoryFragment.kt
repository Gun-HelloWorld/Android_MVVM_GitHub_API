package com.gun.githubapi.ui.user.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.gun.githubapi.R
import com.gun.githubapi.common.BaseFragment
import com.gun.githubapi.common.ErrorMessageParser
import com.gun.githubapi.databinding.FragmentRepositoryBinding
import com.gun.githubapi.ui.user.detail.RepositoryRecyclerAdapter
import com.gun.githubapi.ui.user.detail.UserDetailViewModel
import kotlinx.coroutines.launch

class RepositoryFragment : BaseFragment() {

    private val userDetailViewModel by activityViewModels<UserDetailViewModel> { UserDetailViewModel.Factory }

    private lateinit var binding: FragmentRepositoryBinding
    private val recyclerAdapter = RepositoryRecyclerAdapter()

    companion object {
        fun newInstance() = RepositoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_repository, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = recyclerAdapter
        }

        initObserver()

        userDetailViewModel.fetchRepositoryList()
    }


    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userDetailViewModel.errorStateFlow.collect {
                        val message = ErrorMessageParser.parseToErrorMessage(resources, it)
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
                    }
                }

                launch {
                    userDetailViewModel.repoDataStateFlow.collect {
                        recyclerAdapter.submitList(it.repositoryList)
                    }
                }
            }
        }
    }

}