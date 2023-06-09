package com.gun.githubapi.ui.user.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.gun.githubapi.common.recyclerview.BaseItemCallback
import com.gun.githubapi.common.recyclerview.BaseViewHolder
import com.gun.githubapi.common.recyclerview.ItemClickTransitionListener
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.databinding.HolderUserSearchBinding

class UserSearchRecyclerAdapter(val listener: ItemClickTransitionListener<User>) :
    PagingDataAdapter<User, UserSearchRecyclerAdapter.UserViewHolder>(BaseItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            HolderUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(holder.bindingAdapterPosition)?.let {
            holder.setData(it)
        }
    }

    inner class UserViewHolder(private val binding: HolderUserSearchBinding) :
        BaseViewHolder(binding.root) {
        fun setData(user: User) {
            binding.user = user
            binding.listener = listener
        }
    }
}