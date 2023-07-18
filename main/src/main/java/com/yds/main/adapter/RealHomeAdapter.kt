package com.yds.main.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.yds.main.data.RealHomeData
import com.yds.main.provider.RealHomeAppEntryProvider

class RealHomeAdapter : BaseProviderMultiAdapter<RealHomeData>() {

    companion object {
        const val APP_ENTRY = 0
    }

    init {
        addItemProvider(RealHomeAppEntryProvider())
    }

    override fun getItemType(data: List<RealHomeData>, position: Int): Int {
        return data[position].funcItemData?.itemType ?: 0
    }


}