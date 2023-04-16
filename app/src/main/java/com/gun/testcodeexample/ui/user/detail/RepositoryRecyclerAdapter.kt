package com.gun.testcodeexample.ui.user.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gun.testcodeexample.common.recyclerview.BaseListAdapter
import com.gun.testcodeexample.common.recyclerview.BaseViewHolder
import com.gun.testcodeexample.data.dto.repository.Repository
import com.gun.testcodeexample.databinding.HolderRepositoryBinding

class RepositoryRecyclerAdapter :
    BaseListAdapter<Repository, RepositoryRecyclerAdapter.RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = HolderRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val data = currentList[holder.bindingAdapterPosition]
        holder.setData(data)
    }

    inner class RepositoryViewHolder(val view: HolderRepositoryBinding) :
        BaseViewHolder(view.root) {
        fun setData(repository: Repository) {
            view.repository = repository
        }
    }
}