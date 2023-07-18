package com.yds.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.crystallake.resources.RouterPath
import com.yds.main.R
import com.yds.main.data.FuncItem
import com.yds.main.databinding.ItemAppEntryItemBinding

class AppEntryAdapter : BaseQuickAdapter<FuncItem, BaseViewHolder>(R.layout.item_app_entry_item) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemAppEntryItemBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: FuncItem) {
        val binding = DataBindingUtil.getBinding<ItemAppEntryItemBinding>(holder.itemView)
        if (item.localIcon != null) {
            binding?.icon?.setImageResource(item.localIcon)
            binding?.title?.text = item.title
        }
        holder.itemView.setOnClickListener {
            ARouter.getInstance().build(RouterPath.MAIN_ACTIVITY).navigation()
        }
    }
}