package com.yds.main.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(val space: Int, val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager is GridLayoutManager) {
            outRect.right = space
            outRect.bottom = space
            if (parent.getChildLayoutPosition(view) % spanCount == 0) {
                outRect.left = space
            }
            if (parent.getChildLayoutPosition(view) < spanCount){
                outRect.top = space
            }
        }
    }

}