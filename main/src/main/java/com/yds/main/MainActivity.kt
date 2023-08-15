package com.yds.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.google.android.material.navigation.NavigationBarView
import com.yds.base.BaseDataBindingActivity
import com.yds.core.LoginTool
import com.yds.core.UserInfoTool
import com.yds.main.adapter.NavigationFragmentStateAdapter
import com.yds.main.databinding.ActivityMainBinding
import com.yds.main.databinding.DrawerHeaderBinding


@Route(path = RouterPath.MAIN_ACTIVITY)
class MainActivity : BaseDataBindingActivity<ActivityMainBinding, BaseViewModel>() {

    private val adapter by lazy {
        NavigationFragmentStateAdapter(this)
    }
    private var drawerHeaderBinding: DrawerHeaderBinding? = null

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }

    @SuppressLint("MissingPermission")
    override fun initData() {
        super.initData()
        initImmersionBar(R.color.color_333333)

        val header = mBinding?.navigation?.getHeaderView(0)
        header?.let {
            drawerHeaderBinding = DrawerHeaderBinding.bind(it)
        }

        drawerHeaderBinding?.login?.setOnClickListener {
            mBinding?.drawerLayout?.closeDrawers()
            if (UserInfoTool.getLoginState()) {
                ARouter.getInstance().build(RouterPath.MINE_ACTIVITY).navigation()
            } else {
                ARouter.getInstance().build(RouterPath.LOGIN_ACTIVITY).navigation()
            }
        }
        drawerHeaderBinding?.header?.setOnClickListener {
            mBinding?.drawerLayout?.closeDrawers()
            if (UserInfoTool.getLoginState()) {
                ARouter.getInstance().build(RouterPath.MINE_ACTIVITY).navigation()
            } else {
                ARouter.getInstance().build(RouterPath.LOGIN_ACTIVITY).navigation()
            }
        }


        val toggle = MyActionBarDrawerToggle(
            this,
            mBinding?.drawerLayout,
            mBinding?.homeToolbar,
            R.string.open,
            R.string.close
        ) {
            initDrawerLayout()
        }

        toggle.syncState()
        mBinding?.drawerLayout?.addDrawerListener(toggle)

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
                R.id.tab_navigation -> {
                    println("导航")
                    mBinding?.viewPager?.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                R.id.tab_project -> {
                    println("项目")
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
                        mBinding?.title?.text = getString(R.string.home_pager)
                    }
                    1 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_knowledge_hierarchy
                        mBinding?.title?.text = getString(R.string.knowledge_hierarchy)
                    }
                    2 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_navigation
                        mBinding?.title?.text = getString(R.string.navigation)
                    }
                    3 -> {
                        mBinding?.bottomNavigation?.selectedItemId = R.id.tab_project
                        mBinding?.title?.text = getString(R.string.project)
                    }
                }
            }
        })
        mBinding?.navigation?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    LoginTool.logout()
                }
                R.id.nav_gallery -> {
                    ARouter.getInstance().build(RouterPath.MAIN_GALLERY_ACTIVITY).navigation()
                }
                R.id.nav_share -> {
                    val intent = Intent(this@MainActivity, TestActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_collect -> {
                    if (UserInfoTool.getLoginState()) {
                        ARouter.getInstance().build(RouterPath.MAIN_COLLECT_ACTIVITY).navigation()
                    } else {
                        ARouter.getInstance().build(RouterPath.LOGIN_ACTIVITY).navigation()
                    }
                }
            }
            mBinding?.drawerLayout?.closeDrawers()
            Toast.makeText(this, "点击了：${menuItem.title}", Toast.LENGTH_SHORT).show()
            false
        }

    }

    inner class MyActionBarDrawerToggle(
        activity: Activity,
        drawerLayout: DrawerLayout?,
        toolbar: Toolbar?,
        openDrawerContentDescRes: Int,
        closeDrawerContentDescRes: Int,
        private val drawerListener: (Boolean) -> Unit
    ) : ActionBarDrawerToggle(
        activity,
        drawerLayout,
        toolbar,
        openDrawerContentDescRes,
        closeDrawerContentDescRes
    ) {
        override fun onDrawerClosed(drawerView: View) {
            drawerListener.invoke(false)
            super.onDrawerClosed(drawerView)
        }

        override fun onDrawerOpened(drawerView: View) {
            drawerListener.invoke(true)
            super.onDrawerOpened(drawerView)
        }
    }

    private fun initDrawerLayout() {
        if (UserInfoTool.getLoginState()) {
            drawerHeaderBinding?.login?.isVisible = false
            drawerHeaderBinding?.group?.isVisible = true
            drawerHeaderBinding?.email?.text = UserInfoTool.getUserName()
        } else {
            drawerHeaderBinding?.group?.isVisible = false
            drawerHeaderBinding?.login?.isVisible = true
        }
    }

    override fun onResume() {
        super.onResume()
        initDrawerLayout()
    }
}
