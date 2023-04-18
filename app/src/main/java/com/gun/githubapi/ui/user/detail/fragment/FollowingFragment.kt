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
import com.gun.githubapi.databinding.FragmentInterestingBinding
import com.gun.githubapi.ui.user.detail.InterestingUserRecyclerAdapter
import com.gun.githubapi.ui.user.detail.UserDetailViewModel
import kotlinx.coroutines.launch

class FollowingFragment : BaseFragment() {
    private val userDetailViewModel by activityViewModels<UserDetailViewModel> { UserDetailViewModel.Factory }

    private lateinit var binding: FragmentInterestingBinding
    private val recyclerAdapter = InterestingUserRecyclerAdapter(null)

    companion object {
        fun newInstance() = FollowingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_interesting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.adapter = recyclerAdapter
        }

        initObserver()

        userDetailViewModel.fetchFollowingList()
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
                    userDetailViewModel.followingDataStateFlow.collect {
                        recyclerAdapter.submitList(it.followingList)
                    }
                }
            }
        }
    }
}