package com.yds.main

import android.net.Uri
import android.os.Bundle
import android.transition.Transition
import androidx.core.view.ViewCompat
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.BaseDataBindingFragment
import com.yds.main.adapter.TransitionListenerAdapter
import com.yds.main.databinding.FragmentImageBinding
import com.yds.main.vm.GalleryViewModel
import java.io.File

class ImageFragment : BaseDataBindingFragment<FragmentImageBinding, BaseViewModel>() {

    private var mTransition: Transition? = null

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        val arguments = arguments
        val path = arguments?.getString(KEY_IMAGE_RES)
        path?.let {
            mBinding?.image?.transitionName = it
            val uri = Uri.fromFile(File(it))
            mBinding?.image?.setImageURI(uri)
        }
//        initTransition()
    }

    override fun lazyLoadData() {
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image)
    }

    companion object {
        const val KEY_IMAGE_RES = "com.yds.main.ImageFragment.imageRes"
        fun newInstance(path: String): ImageFragment {
            val fragment = ImageFragment()
            val argument = Bundle()
            argument.putString(KEY_IMAGE_RES, path)
            fragment.arguments = argument
            return fragment
        }
    }

    private fun initTransition() {
        //因为进入视频详情页面后还需请求数据，所以在过渡动画完成后在请求数据
        //延迟动画执行
        postponeEnterTransition()
        mBinding?.image?.let {
            //设置共用元素对应的View
            ViewCompat.setTransitionName(it, GalleryViewModel.SHARED_ELEMENT_NAME)
            //获取共享元素进入转场对象
            mTransition = requireActivity().window.sharedElementEnterTransition
            //设置共享元素动画执行完成的回调事件
            mTransition?.addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition?) {
                    //移除共享元素动画监听事件
                    mTransition?.removeListener(this)
                }
            })
        }

        //开始动画执行
        startPostponedEnterTransition()
    }
}