package com.yds.project.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import com.yds.home.model.BaseArticle
import com.yds.project.databinding.ProjectArticleItemBinding

class ProjectArticleItem(val projectModel: BaseArticle) : ItemProxy<ProjectArticleItemBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ProjectArticleItemBinding {
        return ProjectArticleItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: ProjectArticleItemBinding
    ) {
        binding.title.text = projectModel.title
        binding.author.text = projectModel.author
        binding.time.text = projectModel.niceDate
        Glide.with(binding.root.context).load(projectModel.envelopePic).into(binding.envelopePic)
        binding.container.setOnClickListener {
            ARouter.getInstance().build(RouterPath.BROWSER_ACTIVITY).apply {
                extras.putString(ARTICLE_URL, projectModel.link)
                extras.putString(ARTICLE_TITLE, projectModel.title)
            }.navigation()
        }
    }
}