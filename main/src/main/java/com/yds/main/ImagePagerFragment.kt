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
        mBinding?.viewPager?.currentItem = GalleryActivity.currentPosition
        mBinding?.viewPager?.addOnPageChangeListener(object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                GalleryActivity.currentPosition = position
            }
        })
        prepareSharedElementTransition()
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image_pager)
    }

    fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                var view: View? = null
                mBinding?.viewPager?.let {
                    val currentFragment = it.adapter
                        ?.instantiateItem(it, GalleryActivity.currentPosition)
                    view = (currentFragment as? ImageFragment)?.view
                }
                view?.let {
                    sharedElements?.put(names?.get(0) ?: "", it.findViewById(R.id.image))
                }
            }
        })
    }
}