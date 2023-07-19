package com.yds.project.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import com.yds.home.fjs.model.BaseArticle
import com.yds.project.databinding.ProjectArticleItemBinding

class ProjectArticleAdapter(val dataList: MutableList<BaseArticle>?) :
    RecyclerView.Adapter<ProjectArticleAdapter.ProjectArticleViewHolder>() {


    fun clear() {
        dataList?.clear()
    }

    inner class ProjectArticleViewHolder(val binding: ProjectArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectArticleViewHolder {
        val binding = ProjectArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectArticleViewHolder, position: Int) {
        val startTime = System.nanoTime()
        val binding = holder.binding
        if (dataList.isNullOrEmpty()) {
            return
        }
        val projectModel = dataList[position]
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
        Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }
}