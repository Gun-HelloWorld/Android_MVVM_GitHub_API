package com.gun.testcodeexample.ui.user.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gun.testcodeexample.R

class LoadingStateAdapter : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    inner class LoadingStateViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        fun bind(loadState: LoadState) {
            val loadingBar = view.findViewById<ContentLoadingProgressBar>(R.id.loading_bar)
            loadingBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_loading_bar_for_paging_adapter, parent, false)
        return LoadingStateViewHolder(view)
    }
}
