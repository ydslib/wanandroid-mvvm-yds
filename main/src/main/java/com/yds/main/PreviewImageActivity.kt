package com.yds.main

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.main.databinding.ActivityPreviewImageBinding
import com.yds.main.vm.GalleryViewModel

@Route(path = RouterPath.MAIN_PREVIEW_IMAGE_ACTIVITY)
class PreviewImageActivity : BaseDataBindingActivity<ActivityPreviewImageBinding, GalleryViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_preview_image)
    }

    override fun initData() {
        super.initData()
        val position = intent.extras?.getInt("position") ?: 0
        val fragment = ImagePagerFragment()
        fragment.arguments = Bundle()
        fragment.arguments?.putInt("position", position)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}