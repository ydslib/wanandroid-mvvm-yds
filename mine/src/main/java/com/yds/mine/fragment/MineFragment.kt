package com.yds.mine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.yds.mine.R
import com.yds.mine.databinding.FragmentMineBinding
import com.yds.mine.vm.MineViewModel

@Route(path = RouterPath.MINE_FRAGMENT)
class MineFragment : DataBindingFragment<FragmentMineBinding, MineViewModel>() {

    private val mActivityViewModel: MineViewModel? by lazy {
        getActivityScopeViewModel(MineViewModel::class.java)
    }

    override fun createObserver() {
        mActivityViewModel?.coinLiveData?.observe(this) {
            mBinding?.username?.text = it?.username
            mBinding?.rankNum?.text = it?.rank?.toString()
            mBinding?.coinNum?.text = it?.coinCount?.toString()
        }
        mActivityViewModel?.collectTotal?.observe(this) {
            mBinding?.collectNum?.text = it.toString()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initDefaultPage()
        mBinding?.collect?.setOnClickListener {
            val collectFragment = ARouter.getInstance().build(RouterPath.MINE_COLLECT_FRAGMENT)
                .navigation() as Fragment
            childFragmentManager.beginTransaction().replace(R.id.container, collectFragment)
                .commit()
        }

        mBinding?.back?.setOnClickListener {
            requireActivity().finish()
        }
        mBinding?.scan?.setOnClickListener {
            ARouter.getInstance().build(RouterPath.SCAN_OCR_ACTIVITY).navigation()
        }
    }

    fun initDefaultPage() {
        val collectFragment = ARouter.getInstance().build(RouterPath.MINE_COLLECT_FRAGMENT)
            .navigation() as Fragment
        childFragmentManager.beginTransaction().replace(R.id.container, collectFragment)
            .commit()
    }

    override fun lazyLoadData() {
        mActivityViewModel?.getCoin()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_mine)
    }
}