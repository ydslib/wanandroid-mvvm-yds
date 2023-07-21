package com.yds.eyepetizer.home.adapter

import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.yds.eyepetizer.home.item.BannerItem
import com.yds.eyepetizer.home.model.ProviderMultiModel

class HomeAdapter(owner: LifecycleOwner) : BaseProviderMultiAdapter<ProviderMultiModel>() {

    init {
        addItemProvider(BannerItem(owner))
    }

    override fun getItemType(data: List<ProviderMultiModel>, position: Int): Int {
        return data[position].type
    }
}