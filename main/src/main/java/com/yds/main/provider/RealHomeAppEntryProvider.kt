package com.yds.main.provider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yds.main.R
import com.yds.main.adapter.AppEntryAdapter
import com.yds.main.adapter.RealHomeAdapter
import com.yds.main.adapter.SpaceItemDecoration
import com.yds.main.data.RealHomeData
import com.yds.main.databinding.ItemAppEntryBinding

class RealHomeAppEntryProvider : BaseItemProvider<RealHomeData>() {
    override val itemViewType: Int
        get() = RealHomeAdapter.APP_ENTRY
    override val layoutId: Int
        get() = R.layout.item_app_entry

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemAppEntryBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: RealHomeData) {
        val binding = DataBindingUtil.getBinding<ItemAppEntryBinding>(helper.itemView)
        val data = item.funcItemData?.funcItemList
        val layoutManager = GridLayoutManager(helper.itemView.context, 5, GridLayoutManager.VERTICAL, false)
        binding?.entryRecycler?.let {
            it.layoutManager = layoutManager
            val adapter = AppEntryAdapter().apply {
                this.setList(data)
            }
            it.addItemDecoration(SpaceItemDecoration(20,5))
            it.adapter = adapter
        }
    }
}