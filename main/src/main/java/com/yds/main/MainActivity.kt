package com.yds.main

import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.gyf.immersionbar.ktx.immersionBar
import com.yds.main.adapter.NavigationFragmentStateAdapter
import com.yds.main.databinding.ActivityMainBinding

class MainActivity : DataBindingActivity<ActivityMainBinding, BaseViewModel>() {

    val adapter by lazy {
        NavigationFragmentStateAdapter(this,2)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.color_333333)
        }

        mBinding?.viewPager?.adapter = adapter
        mBinding?.bottomNavigation?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_main_pager -> {
                    println("首页")
                    mBinding?.viewPager?.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.tab_knowledge_hierarchy -> {
                    println("知识体系")
                    mBinding?.viewPager?.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.tab_wx_article -> {
                    println("公众号")
                    return@setOnItemSelectedListener true
                }
                R.id.tab_navigation -> {
                    println("导航")
                    return@setOnItemSelectedListener true
                }
                R.id.tab_project -> {
                    println("项目")
                    return@setOnItemSelectedListener true
                }
            }
            false
        }


    }
}
