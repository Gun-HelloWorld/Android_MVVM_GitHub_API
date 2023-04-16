package com.gun.githubapi.common.recyclerview

import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T : Any, VH : BaseViewHolder> :
    ListAdapter<T, VH>(BaseItemCallback<T>()) {
}

