package com.yds.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.crystallake.base.activity.DataBindingActivity
import com.gyf.immersionbar.ktx.immersionBar

abstract class BaseDataBindingActivity<D : ViewDataBinding, VM : ViewModel> :
    DataBindingActivity<D, VM>() {

    fun initImmersionBar(statusBarColor: Int) {
        immersionBar {
            statusBarColor(statusBarColor)
            applySystemFits(true)
        }
    }
}