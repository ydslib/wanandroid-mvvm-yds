package com.yds.eyepetizer

import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.eyepetizer.adapter.NavigationFragmentStateAdapter
import com.yds.eyepetizer.databinding.ActivityEyepetizerBinding


@Route(path = RouterPath.EYEPETIZER_ACTIVITY)
class EyepetizerActivity : BaseDataBindingActivity<ActivityEyepetizerBinding, BaseViewModel>() {

    val adapter by lazy {
        NavigationFragmentStateAdapter(this)
    }

    override fun initData() {
        super.initData()
        mBinding?.bottomNavigation?.itemIconTintList = null
        mBinding?.viewPager?.let {
            it.adapter = adapter
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            mBinding?.bottomNavigation?.selectedItemId = R.id.item_home
                        }
                        1 -> {
                            mBinding?.bottomNavigation?.selectedItemId = R.id.item_discovery
                        }
                        2 -> {
                            mBinding?.bottomNavigation?.selectedItemId = R.id.item_hot
                        }
                        3 -> {
                            mBinding?.bottomNavigation?.selectedItemId = R.id.item_mine
                        }
                    }
                }
            })
        }
        mBinding?.bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    mBinding?.viewPager?.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.item_discovery -> {
                    mBinding?.viewPager?.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.item_hot -> {
                    mBinding?.viewPager?.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                R.id.item_mine -> {
                    mBinding?.viewPager?.currentItem = 3
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_eyepetizer)
    }
}