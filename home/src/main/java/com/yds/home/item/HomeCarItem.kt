package com.yds.home.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide

import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import com.yds.home.R
import com.yds.home.databinding.HomeCarItemBinding
import com.yds.home.model.BaseArticle

class HomeCarItem(
    val baseArticle: BaseArticle,
    val clickListener: ((position: Int, collect: Boolean) -> Unit)? = null
) : ItemProxy<HomeCarItemBinding>() {
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
        Glide.with(holder.itemView.context).load(R.drawable.ic_launcher_background)
            .into(binding.homeHeaderImg)
        binding.author.text =
            if (baseArticle.author.isNullOrEmpty()) baseArticle.shareUser else baseArticle.author
        binding.superChapterName.text = "${baseArticle.superChapterName}/${baseArticle.chapterName}"
        binding.title.text = baseArticle.title
        if (baseArticle.collect == true) {
            Glide.with(holder.itemView.context).load(R.drawable.icon_like).into(binding.like)
        } else {
            Glide.with(holder.itemView.context).load(R.drawable.icon_like_article_not_selected)
                .into(binding.like)
        }
        binding.timeTv.text = baseArticle.niceShareDate

        binding.like.setOnClickListener {
            clickListener?.invoke(position, baseArticle.collect ?: false)
        }
        binding.root.setOnClickListener {
            ARouter.getInstance().build(RouterPath.BROWSER_ACTIVITY).apply {
                extras.putString(ARTICLE_URL, baseArticle.link)
                extras.putString(ARTICLE_TITLE, baseArticle.title)
            }.navigation()
        }
    }
}