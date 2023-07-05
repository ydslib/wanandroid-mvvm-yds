package com.crystallake.knowledgehierarchy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.TagAdapter
import com.crystallake.knowledgehierarchy.databinding.ItemKnowledgeArticleBinding
import com.crystallake.resources.RouterPath
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yds.project.model.ProjectTitleModel

class KnowledgeAdapter(val dataList: MutableList<ProjectTitleModel>) :
    RecyclerView.Adapter<KnowledgeAdapter.KnowledgeViewHolder>() {
    fun clear() {
        dataList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowledgeViewHolder {
        val binding = ItemKnowledgeArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnowledgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KnowledgeViewHolder, position: Int) {
        val startTime = System.nanoTime()
        val binding = holder.binding
        val itemData = dataList[position]
        holder.currentPosition = position
        binding.title.text = itemData.name
        val tags = mutableListOf<String>()
        itemData.children?.forEach {
            tags.add(it.name ?: "")
        }
        val layoutManager = holder.layoutManager
        binding.tagRecycler.adapter = holder.adapter.apply {
            this.dataList.clear()
            setData(tags)
        }
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.tagRecycler.layoutManager = layoutManager
        val itemDecoration = holder.itemDecoration
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                binding.knowledgeCard.context,
                R.drawable.divider_ffffff
            )
        )
        val itemDecorationCount = binding.tagRecycler.itemDecorationCount
        for (i in itemDecorationCount - 1 downTo 0) {
            binding.tagRecycler.removeItemDecorationAt(i)
        }
        binding.tagRecycler.addItemDecoration(itemDecoration)

        //解决嵌套的recyclerview拦截了container点击事件

        Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class KnowledgeViewHolder(val binding: ItemKnowledgeArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var currentPosition = 0
        val adapter: TagAdapter
        val layoutManager: FlexboxLayoutManager
        val itemDecoration: FlexboxItemDecoration

        init {
            adapter = TagAdapter()
            layoutManager = FlexboxLayoutManager(binding.knowledgeCard.context)
            itemDecoration = FlexboxItemDecoration(binding.knowledgeCard.context)
            binding.tagRecycler.setOnTouchListener { _, event -> binding.container.onTouchEvent(event) }
            binding.container.setOnClickListener {
                ARouter.getInstance().build(RouterPath.KNOWLEDGE_ARTICLE_ACTIVITY)
                    .apply {
                        extras.putInt("position", currentPosition)
                    }.navigation()
            }
        }
    }
}