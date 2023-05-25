package com.yds.main

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView
import com.gyf.immersionbar.ktx.immersionBar
import com.yds.main.adapter.NavigationFragmentStateAdapter
import com.yds.main.databinding.ActivityMainBinding

class MainActivity : DataBindingActivity<ActivityMainBinding, BaseViewModel>() {

    private val adapter by lazy {
        NavigationFragmentStateAdapter(this)
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        immersionBar {
            statusBarColor(R.color.color_333333)
            applySystemFits(true)
        }


        val toggle = ActionBarDrawerToggle(
            this,
            mBinding?.drawerLayout,
            mBinding?.homeToolbar,
            R.string.open,
            R.string.close
        )

        toggle.syncState()
        mBinding?.drawerLayout?.addDrawerListener(toggle)

        mBinding?.viewPager?.adapter = adapter
        mBinding?.bottomNavigation?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_main_pager -> {
                    println("首页")
                    mBinding?.viewPager?.currentItem = 0
                    mBinding?.homeToolbar?.isVisible = true
                    return@setOnItemSelectedListener true
                }
                R.id.tab_knowledge_hierarchy -> {
                    println("知识体系")
                    mBinding?.homeToolbar?.isVisible = false
                    mBinding?.viewPager?.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.tab_navigation -> {
                    println("导航")
                    mBinding?.homeToolbar?.isVisible = false
                    mBinding?.viewPager?.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                R.id.tab_project -> {
                    println("项目")
                    mBinding?.homeToolbar?.isVisible = false
                    mBinding?.viewPager?.currentItem = 3
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        mBinding?.bottomNavigation?.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        mBinding?.viewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_main_pager
                        mBinding?.homeToolbar?.isVisible = true
                    }
                    1 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_knowledge_hierarchy
                        mBinding?.homeToolbar?.isVisible = false
                    }
                    2 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_navigation
                        mBinding?.homeToolbar?.isVisible = false
                    }
                    3 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_project
                        mBinding?.homeToolbar?.isVisible = false
                    }
                }
            }
        })


    }
}
