package com.crystallake.knowledgehierarchy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.databinding.ItemKnowledgeCardBinding
import com.crystallake.knowledgehierarchy.diff.DiffCallBack
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import com.yds.home.model.BaseArticle

class KnowledgeArticleAdapter(
    val dataList: MutableList<BaseArticle>,
    val clickListener: ((position: Int, collect: Boolean) -> Unit)? = null
) :
    RecyclerView.Adapter<KnowledgeArticleAdapter.KnowledgeArticleViewHolder>() {

    fun clear() {
        dataList.clear()
    }

    inner class KnowledgeArticleViewHolder(val binding: ItemKnowledgeCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var currentPosition = 0

        init {
            Glide.with(binding.root.context).load(R.drawable.ic_launcher_background)
                .into(binding.homeHeaderImg)
            binding.like.setOnClickListener {
                clickListener?.invoke(position, dataList[currentPosition].collect ?: false)
            }
            binding.root.setOnClickListener {
                ARouter.getInstance().build(RouterPath.BROWSER_ACTIVITY).apply {
                    extras.putString(ARTICLE_URL, dataList[currentPosition].link)
                    extras.putString(ARTICLE_TITLE, dataList[currentPosition].title)
                }.navigation()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeArticleViewHolder {
        val binding = ItemKnowledgeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnowledgeArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KnowledgeArticleViewHolder, position: Int) {
        val startTime = System.nanoTime()
        val binding = holder.binding
        val baseArticle = dataList[position]
        holder.currentPosition = position
        binding.author.text =
            if (baseArticle.author.isNullOrEmpty()) baseArticle.shareUser else baseArticle.author
        binding.superChapterName.text = "${baseArticle.superChapterName}/${baseArticle.chapterName}"
        binding.title.text = baseArticle.title
        val glideTime = System.nanoTime()
        if (baseArticle.collect == true) {
            binding.like.setImageResource(R.drawable.icon_like)
        } else {
            binding.like.setImageResource(R.drawable.icon_like_article_not_selected)
        }
        Log.i("onBindViewHolder", ">>${System.nanoTime() - glideTime}")
        binding.timeTv.text = baseArticle.niceShareDate
        Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun update(list: MutableList<BaseArticle>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallBack(list, dataList))
        dataList.clear()
        dataList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}