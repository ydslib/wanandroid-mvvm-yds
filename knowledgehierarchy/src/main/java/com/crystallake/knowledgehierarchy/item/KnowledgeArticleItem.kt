package com.crystallake.knowledgehierarchy.item

import android.view.LayoutInflater
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.knowledgehierarchy.databinding.ItemKnowledgeArticleBinding
import com.crystallake.knowledgehierarchy.model.KnowledgeModel
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
        val sb = StringBuilder()
        itemData.children?.forEach {
            if (!it.name.isNullOrEmpty()) {
                sb.append(it.name).append("   ")
            }
        }
        binding.subTitle.text = sb.toString()
    }
}