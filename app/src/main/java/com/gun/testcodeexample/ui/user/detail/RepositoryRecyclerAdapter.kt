package com.gun.testcodeexample.ui.user.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.gun.testcodeexample.R
import com.gun.testcodeexample.common.recyclerview.BaseListAdapter
import com.gun.testcodeexample.common.recyclerview.BaseViewHolder
import com.gun.testcodeexample.data.dto.repository.Repository

class RepositoryRecyclerAdapter :
    BaseListAdapter<Repository, RepositoryRecyclerAdapter.RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repository_holder, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val data = currentList[holder.adapterPosition]
        holder.setData(data)
    }

    inner class RepositoryViewHolder(private val view: View) : BaseViewHolder(view) {
        private val tvRepoName: TextView by lazy { view.findViewById(R.id.tv_repo_name) }
        private val tvStartCnt: TextView by lazy { view.findViewById(R.id.tv_star_cnt) }
        private val tvWatchCnt: TextView by lazy { view.findViewById(R.id.tv_watch_cnt) }
        private val tvForkCnt: TextView by lazy { view.findViewById(R.id.tv_fork_cnt) }
        private val tvDescription: TextView by lazy { view.findViewById(R.id.tv_description) }
        private val tvLicense: TextView by lazy { view.findViewById(R.id.tv_license) }
        private val groupLicense: Group by lazy { view.findViewById(R.id.group_license) }

        fun setData(repository: Repository) {
            tvRepoName.text = repository.name
            tvStartCnt.text = view.context.getString(R.string.value_with_starts, repository.stargazersCount)
            tvWatchCnt.text = view.context.getString(R.string.value_with_watching, repository.watchersCount)
            tvForkCnt.text = view.context.getString(R.string.value_with_forks, repository.forksCount)
            tvDescription.text = repository.description

            if (repository.license == null) {
                groupLicense.visibility = View.GONE
            } else {
                groupLicense.visibility = View.VISIBLE
                tvLicense.text = repository.license.name
            }
        }
    }
}