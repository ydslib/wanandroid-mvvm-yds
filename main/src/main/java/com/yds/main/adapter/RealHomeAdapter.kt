package com.yds.main.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.yds.main.data.RealHomeData
import com.yds.main.provider.RealHomeAppEntryProvider
import com.yds.main.provider.TitleItem

class RealHomeAdapter : BaseProviderMultiAdapter<RealHomeData>() {

    companion object {
        const val TITLE = 0
        const val APP_ENTRY = 1
    }

    init {
        addItemProvider(TitleItem())
        addItemProvider(RealHomeAppEntryProvider())
    }

    override fun getItemType(data: List<RealHomeData>, position: Int): Int {
        return data[position].funcItemData?.itemType ?: 0
    }


}