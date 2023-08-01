package com.yds.main

import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.resources.RouterPath
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.adapter.GalleryAdapter
import com.yds.main.databinding.FragmentGalleryBinding
import com.yds.main.vm.GalleryViewModel

class GalleryFragment : DataBindingFragment<FragmentGalleryBinding, GalleryViewModel>() {

    private val appViewModel by lazy {
        getApplicationScopeViewModel(GalleryViewModel::class.java)
    }

    private val adapter by lazy {
        GalleryAdapter { v, position ->
            ARouter.getInstance().build(RouterPath.MAIN_PREVIEW_IMAGE_ACTIVITY).apply {
//                withOptionsCompat(
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        requireActivity(),
//                        v,
//                        GalleryViewModel.SHARED_ELEMENT_NAME
//                    )
//                )
            }.withInt("position", position)
                .navigation()
        }
    }

    override fun createObserver() {
        appViewModel?.imageUriList?.observe(this) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.recyclerView?.let {
            it.adapter = adapter
            if (it.layoutManager is FlexboxLayoutManager) {
                (it.layoutManager as? FlexboxLayoutManager)?.apply {
                    flexWrap = FlexWrap.WRAP
                    flexDirection = FlexDirection.ROW
                    alignItems = AlignItems.STRETCH
                }
            }
//            it.layoutManager = flexboxLayoutManager
        }
        appViewModel?.readPic()

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            appViewModel?.getPicData(GalleryViewModel.REFRESH)
            it.finishRefresh()
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            appViewModel?.getPicData(GalleryViewModel.LOADMORE)
            appViewModel?.page = (appViewModel?.page ?: 0) + 1
            it.finishLoadMore()
        }

        mBinding?.back?.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_gallery)
    }
}