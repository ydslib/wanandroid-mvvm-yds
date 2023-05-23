package com.yds.navigation.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.navigation.databinding.NavigationItemBinding
import com.yds.navigation.model.NaviModel

class NaviItem(val item: NaviModel) : ItemProxy<NavigationItemBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NavigationItemBinding {
        return NavigationItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: NavigationItemBinding
    ) {
        binding.title.text = item.name
        val tagDataList = mutableListOf<String>()
        item.articles?.forEach {
            tagDataList.add(it.title ?: "")
        }
        binding.tabLayout.tags = tagDataList
    }
}