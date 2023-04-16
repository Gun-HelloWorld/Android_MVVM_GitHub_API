package com.gun.testcodeexample.ui.user.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gun.testcodeexample.databinding.LayoutLoadingBarForPagingAdapterBinding

class LoadingStateAdapter : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = LayoutLoadingBarForPagingAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadingStateViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingStateViewHolder(
        private val binding: LayoutLoadingBarForPagingAdapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.isLoading = loadState is LoadState.Loading
        }
    }
}
