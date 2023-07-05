package com.yds.base.fastrecycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class ItemViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    open fun initView() {

    }
}