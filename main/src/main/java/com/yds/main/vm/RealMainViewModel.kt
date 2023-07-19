package com.yds.main.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.yds.base.BaseViewModel
import com.yds.main.R
import com.yds.main.adapter.RealHomeAdapter
import com.yds.main.data.FuncItem
import com.yds.main.data.FuncItemData
import com.yds.main.data.RealHomeData

class RealMainViewModel : BaseViewModel() {

    val realHomeLiveData = MutableLiveData<MutableList<RealHomeData>>()

    fun getAppEntryData(context: Context) {
        val appEntryData = mutableListOf<FuncItem>()
        appEntryData.add(FuncItem(localIcon = R.drawable.ic_fju, title = context.getString(R.string.fju)))

        val realHomeData = RealHomeData()
        realHomeData.funcItemData = FuncItemData(itemType = RealHomeAdapter.APP_ENTRY, appEntryData)

        val realHomeDataList = mutableListOf<RealHomeData>()
        realHomeDataList.add(realHomeData)

        realHomeLiveData.value = realHomeDataList
    }
}