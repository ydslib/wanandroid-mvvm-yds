package com.crystallake.knowledgehierarchy

import android.view.LayoutInflater
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.crystallake.knowledgehierarchy.databinding.TagItemBinding

class TagAdapter : SingleDataBindingAdapter<String, TagItemBinding>() {

    override fun generateDataBinding(inflater: LayoutInflater, parent: ViewGroup): TagItemBinding {
        return TagItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(binding: TagItemBinding, position: Int) {
        mBinding?.tagItem?.text = dataList[position]
    }
}