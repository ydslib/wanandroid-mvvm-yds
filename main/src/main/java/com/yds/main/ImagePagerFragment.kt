package com.yds.main

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.crystallake.base.config.DataBindingConfig
import com.yds.base.BaseDataBindingFragment
import com.yds.main.adapter.ImagePagerAdapter
import com.yds.main.databinding.FragmentImagePagerBinding
import com.yds.main.vm.GalleryViewModel

class ImagePagerFragment : BaseDataBindingFragment<FragmentImagePagerBinding, GalleryViewModel>() {

    private val appViewModel by lazy {
        getApplicationScopeViewModel(GalleryViewModel::class.java)
    }

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        val position = arguments?.getInt("position") ?: 0
        val adapter = ImagePagerAdapter(
            appViewModel?.imageUriList?.value ?: arrayListOf(),
            fragmentManager = childFragmentManager
        )
        mBinding?.viewPager?.adapter = adapter
        mBinding?.viewPager?.currentItem = position
        mBinding?.viewPager?.addOnPageChangeListener(object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {

            }
        })
        adapter.notifyDataSetChanged()
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image_pager)
    }
}