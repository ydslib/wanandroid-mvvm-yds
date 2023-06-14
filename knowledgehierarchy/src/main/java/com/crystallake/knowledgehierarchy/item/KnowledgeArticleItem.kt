package com.crystallake.knowledgehierarchy.item

import android.view.LayoutInflater
import android.view.ViewGroup
import co.lujun.androidtagview.TagView
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.knowledgehierarchy.databinding.ItemKnowledgeArticleBinding
import com.crystallake.knowledgehierarchy.model.KnowledgeModel
import com.crystallake.resources.ARTICLE_TITLE
import com.crystallake.resources.ARTICLE_URL
import com.crystallake.resources.RouterPath
import java.lang.StringBuilder

class KnowledgeArticleItem(private val itemData: KnowledgeModel) :
    ItemProxy<ItemKnowledgeArticleBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemKnowledgeArticleBinding {
        return ItemKnowledgeArticleBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: ItemKnowledgeArticleBinding
    ) {
        binding.title.text = itemData.name
        val tags = mutableListOf<String>()
        itemData.children?.forEach {
            if (!it.name.isNullOrEmpty()) {
                tags.add(it.name)
            }
        }
        binding.subTitle.tags = tags
        binding.subTitle.setOnTagClickListener(object :TagView.OnTagClickListener{
            override fun onTagClick(position: Int, text: String?) {
//                ARouter.getInstance().build(RouterPath.BROWSER_ACTIVITY).apply {
//                    extras.putString(ARTICLE_URL,tags[position])
//                    extras.putString(ARTICLE_TITLE,tags)
//                }.navigation()
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