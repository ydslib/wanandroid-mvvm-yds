package com.yds.base.fastrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.yds.base.fastrecycler.viewholder.ItemViewHolder

abstract class ItemProxy<VB : ViewBinding> {

    val itemViewType: Int get() = this.javaClass.simpleName.hashCode()

    var mBinding: VB? = null

    open fun onCreateViewHolder(parent: ViewGroup): ItemViewHolder {
        val binding = generateItemViewBinding(LayoutInflater.from(parent.context), parent)
        return ItemViewHolder(binding)
    }

    open fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        (holder.binding as? VB)?.let {
            mBinding = it
            onBindItemViewHolder(holder, position, it)
        }
    }

    abstract fun generateItemViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    abstract fun onBindItemViewHolder(holder: ItemViewHolder, position: Int, binding: VB)
}