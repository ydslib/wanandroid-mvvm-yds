package com.yds.mine

import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.gyf.immersionbar.ktx.immersionBar
import com.yds.base.BaseDataBindingActivity
import com.yds.mine.databinding.ActivityMineBinding
import com.yds.mine.fragment.MineFragment
import com.yds.mine.vm.MineViewModel

@Route(path = RouterPath.MINE_ACTIVITY)
class MineActivity : BaseDataBindingActivity<ActivityMineBinding, MineViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_mine)
    }

    override fun initData() {
        super.initData()
        initImmersionBar(R.color.white)
        supportFragmentManager.beginTransaction().add(R.id.mine_container, MineFragment()).commit()
    }

}