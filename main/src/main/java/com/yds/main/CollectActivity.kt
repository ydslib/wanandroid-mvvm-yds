package com.yds.main

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.main.databinding.ActivityCollectBinding

@Route(path = RouterPath.MAIN_COLLECT_ACTIVITY)
class CollectActivity : BaseDataBindingActivity<ActivityCollectBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_collect)
    }

    override fun initData() {
        initImmersionBar(R.color.color_333333)
        val fragment =
            ARouter.getInstance().build(RouterPath.MINE_COLLECT_FRAGMENT).navigation() as Fragment
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}