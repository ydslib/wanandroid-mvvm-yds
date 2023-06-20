package com.yds.main

import android.os.Bundle
import com.crystallake.base.config.DataBindingConfig
import com.yds.base.BaseDataBindingFragment
import com.yds.main.adapter.ImagePagerAdapter
import com.yds.main.databinding.FragmentImagePagerBinding
import com.yds.main.vm.GalleryViewModel

class ImagePagerFragment : BaseDataBindingFragment<FragmentImagePagerBinding, GalleryViewModel>() {

    private val activityViewModel by lazy {
        getActivityScopeViewModel(GalleryViewModel::class.java)
    }

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewPager?.adapter = ImagePagerAdapter(
            activityViewModel?.imageUriList?.value ?: arrayListOf(),
            fragmentManager = childFragmentManager
        )
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image_pager)
    }
}