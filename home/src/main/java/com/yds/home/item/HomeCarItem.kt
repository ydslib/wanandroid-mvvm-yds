package com.yds.home.item

import android.view.LayoutInflater
import android.view.ViewGroup

import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.home.databinding.HomeCarItemBinding
import com.yds.home.model.BaseArticle

class HomeCarItem(val baseArticle: BaseArticle) : ItemProxy<HomeCarItemBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HomeCarItemBinding {
        return HomeCarItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: HomeCarItemBinding
    ) {
        binding.author.text = baseArticle.author
        binding.superChapterName.text = baseArticle.superChapterName
        binding.chapterName.text = baseArticle.chapterName
    }
}