package com.crystallake.knowledgehierarchy.item

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.knowledgehierarchy.R
import com.crystallake.knowledgehierarchy.TagAdapter
import com.crystallake.knowledgehierarchy.databinding.ItemKnowledgeArticleBinding
import com.crystallake.resources.RouterPath
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.yds.project.model.ProjectTitleModel

class KnowledgeArticleItem(private val itemData: ProjectTitleModel) :
    ItemProxy<ItemKnowledgeArticleBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemKnowledgeArticleBinding {
        return ItemKnowledgeArticleBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: ItemKnowledgeArticleBinding
    ) {
        binding.title.text = itemData.name
        val tags = mutableListOf<String>()
        itemData.children?.forEach {
            tags.add(it.name ?: "")
        }
        val layoutManager = FlexboxLayoutManager(binding.knowledgeCard.context)
        binding.tagRecycler.adapter = TagAdapter().apply {
            setData(tags)
        }
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.tagRecycler.layoutManager = layoutManager
        val itemDecoration = FlexboxItemDecoration(binding.knowledgeCard.context)
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

        binding.container.setOnClickListener {
            ARouter.getInstance().build(RouterPath.KNOWLEDGE_ARTICLE_ACTIVITY)
                .apply {
                    extras.putInt("position", position)
                }.navigation()
        }
        //解决嵌套的recyclerview拦截了container点击事件
        binding.tagRecycler.setOnTouchListener { _, event -> binding.container.onTouchEvent(event) }
    }
}