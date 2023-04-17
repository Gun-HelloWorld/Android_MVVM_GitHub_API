package com.gun.githubapi.ui.user.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.gun.githubapi.common.recyclerview.BaseListAdapter
import com.gun.githubapi.common.recyclerview.BaseViewHolder
import com.gun.githubapi.common.recyclerview.ItemClickListener
import com.gun.githubapi.data.dto.user.User
import com.gun.githubapi.databinding.HolderUserInterestingBinding

class InterestingUserRecyclerAdapter(val listener: ItemClickListener<User>?) :
    BaseListAdapter<User, InterestingUserRecyclerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            HolderUserInterestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(holder.bindingAdapterPosition)?.let {
            holder.setData(it)
        }
    }

    inner class UserViewHolder(private val binding: HolderUserInterestingBinding) :
        BaseViewHolder(binding.root) {
        fun setData(user: User) {
            binding.user = user

            if (listener != null) {
                binding.listener = listener
            }
        }
    }
}