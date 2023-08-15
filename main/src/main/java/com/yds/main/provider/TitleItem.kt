package com.yds.main.provider

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yds.main.R
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.yds.main.adapter.RealHomeAdapter
import com.yds.main.data.RealHomeData
import com.yds.main.databinding.ItemTitleBinding

class TitleItem : BaseItemProvider<RealHomeData>() {
    override val itemViewType: Int
        get() = RealHomeAdapter.TITLE
    override val layoutId: Int
        get() = R.layout.item_title

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemTitleBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: RealHomeData) {
        val binding = DataBindingUtil.getBinding<ItemTitleBinding>(helper.itemView)
        binding?.title?.text = item.titleItemData?.title
    }
}