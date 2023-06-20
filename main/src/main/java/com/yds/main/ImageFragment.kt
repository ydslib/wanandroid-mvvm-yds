package com.yds.main

import android.net.Uri
import android.os.Bundle
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.base.BaseDataBindingFragment
import com.yds.main.databinding.FragmentImageBinding
import java.io.File

class ImageFragment : BaseDataBindingFragment<FragmentImageBinding, BaseViewModel>() {

    override fun createObserver() {

    }

    override fun initView(savedInstanceState: Bundle?) {
        val arguments = arguments
        val path = arguments?.getString(KEY_IMAGE_RES)
        path?.let {
            mBinding?.image?.transitionName = it
            val uri = Uri.fromFile(File(it))
            mBinding?.image?.setImageURI(uri)
            parentFragment?.startPostponedEnterTransition()
        }

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
}