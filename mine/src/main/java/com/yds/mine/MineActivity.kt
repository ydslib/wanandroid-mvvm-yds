package com.yds.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.gyf.immersionbar.ktx.immersionBar
import com.yds.mine.databinding.ActivityMineBinding
import com.yds.mine.fragment.MineFragment
import com.yds.mine.vm.MineViewModel

@Route(path = RouterPath.MINE_ACTIVITY)
class MineActivity : DataBindingActivity<ActivityMineBinding, MineViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_mine)
    }

    override fun initData() {
        super.initData()
        immersionBar {
            statusBarColor(R.color.white)
            applySystemFits(true)
        }
        supportFragmentManager.beginTransaction().add(R.id.mine_container, MineFragment()).commit()
    }

}