package com.gun.githubapi.common.recyclerview

import android.view.View

interface ItemClickTransitionListener<T> {
    fun onItemClick(view: View, transitionView: View, data: T)
}