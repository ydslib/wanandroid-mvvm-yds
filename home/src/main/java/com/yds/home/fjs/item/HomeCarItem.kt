package com.yds.home.fjs.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import com.yds.base.fastrecycler.ItemProxy
import com.yds.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.home.R
import com.yds.home.databinding.HomeCarItemBinding
import com.yds.home.fjs.model.BaseArticle

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

    override fun onCreateViewHolder(parent: ViewGroup): ItemViewHolder {
        val binding = generateItemViewBinding(LayoutInflater.from(parent.context), parent)
        val holder = HomeCarViewHolder(binding)
        holder.initView()
        return holder
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: HomeCarItemBinding
    ) {
        Glide.with(holder.itemView.context)
            .load("https://p0.ssl.qhimgs1.com/sdr/400__/t017afc338f54c4a166.jpg")
            .thumbnail(0.2f)
            .into(binding.homeHeaderImg)
        binding.author.text =
            if (baseArticle.author.isNullOrEmpty()) baseArticle.shareUser else baseArticle.author
        binding.superChapterName.text = "${baseArticle.superChapterName}/${baseArticle.chapterName}"
        binding.title.text = baseArticle.title
        if (baseArticle.collect == true) {
            binding.like.setImageResource(R.drawable.icon_like)
        } else {
            binding.like.setImageResource(R.drawable.icon_like_article_not_selected)
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

    inner class HomeCarViewHolder(val homeCarItemBinding: HomeCarItemBinding) : ItemViewHolder(homeCarItemBinding)
}