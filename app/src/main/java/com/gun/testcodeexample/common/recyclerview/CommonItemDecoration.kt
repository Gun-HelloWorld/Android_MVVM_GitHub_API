package com.gun.testcodeexample.common.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CommonItemDecoration(
    private var top: Int = 0,
    private var bottom: Int = 0,
    private var left: Int = 0,
    private var right: Int = 0,
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = top
        }

        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right

    }

}