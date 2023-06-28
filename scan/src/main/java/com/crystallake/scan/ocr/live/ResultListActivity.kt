package com.crystallake.scan.ocr.live

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.crystallake.scan.R
import com.crystallake.scan.databinding.ActivityResultListBinding
import com.crystallake.scan.databinding.ItemScanResultBinding
import com.crystallake.scan.ocr.TextAdapter
import com.google.gson.reflect.TypeToken
import com.yds.base.BaseDataBindingActivity
import com.yds.base.GsonUtil

@Route(path = RouterPath.SCAN_RESULT_LIST_ACTIVITY)
class ResultListActivity : BaseDataBindingActivity<ActivityResultListBinding, BaseViewModel>() {

    private val adapter by lazy {
        TextAdapter()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_result_list)
    }

    override fun initData() {
        super.initData()
        val resultSet: String = intent.extras?.getString("scanResult") ?: ""
        val type = object : TypeToken<List<String>>() {}.type
        val resultList = GsonUtil.gson.fromJson<List<String>>(resultSet, type)
        mBinding?.recycle?.adapter = adapter
        adapter.dataList.addAll(resultList)
        adapter.notifyDataSetChanged()
    }

}