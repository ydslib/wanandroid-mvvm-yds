package com.crystallake.scan

import android.content.Intent
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.scan.databinding.ActivityScanBinding
import com.crystallake.scan.manager.IManager
import com.crystallake.scan.manager.OnActivityResult
import com.crystallake.scan.manager.ScanManagerFactory

class ScanActivity : DataBindingActivity<ActivityScanBinding, BaseViewModel>() {

    private val scanManagerFactory by lazy {
        ScanManagerFactory()
    }
    private val manager: IManager<String> by lazy {
        scanManagerFactory.createOcrManager()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_scan)
    }

    override fun initData() {
        super.initData()
        mBinding?.takePhoto?.setOnClickListener {
            manager.takePhoto(this)
        }

        mBinding?.test?.setOnClickListener {
            println("test")
        }

        manager.setOnResultListener(object : OnActivityResult<String> {
            override fun onActivityResult(t: String?) {
                mBinding?.recognize?.text = t
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        manager.onActivityResult(this, requestCode, resultCode, data)
    }
}