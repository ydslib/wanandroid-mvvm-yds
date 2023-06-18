package com.yds.navigation.item

import android.view.LayoutInflater
import android.view.ViewGroup
import co.lujun.androidtagview.TagView
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
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
        binding.tabLayout.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onTagClick(position: Int, text: String?) {
                ARouter.getInstance().build(RouterPath.BROWSER_ACTIVITY).apply {
                    extras.putString(ARTICLE_URL, item.articles?.get(position)?.link)
                    extras.putString(ARTICLE_TITLE, item.articles?.get(position)?.title)
                }.navigation()
            }

            override fun onTagLongClick(position: Int, text: String?) {

            }

            override fun onSelectedTagDrag(position: Int, text: String?) {

            }

            override fun onTagCrossClick(position: Int) {

            }

        })
    }
}