package com.yds.main

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.base.BaseDataBindingActivity
import com.yds.main.adapter.GalleryAdapter
import com.yds.main.databinding.ActivityGalleryBinding
import com.yds.main.vm.GalleryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.jar.Manifest


@Route(path = RouterPath.MAIN_GALLERY_ACTIVITY)
class GalleryActivity : BaseDataBindingActivity<ActivityGalleryBinding, GalleryViewModel>() {

    companion object {
        val MANIFEST = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    }

    private val adapter by lazy {
        GalleryAdapter()
    }

    private val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
        flexWrap = FlexWrap.WRAP
        flexDirection = FlexDirection.ROW
        alignItems = AlignItems.STRETCH
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_gallery)
    }


    override fun initData() {
        super.initData()
        initImmersionBar(R.color.color_333333)
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                var granted = true
                it.entries.forEach { entry ->
                    if (!entry.value) {
                        granted = false
                        return@forEach
                    }
                }
                if (granted) {
                    initView()
                } else {
                    Toast.makeText(this, "未申请权限", Toast.LENGTH_SHORT).show()
                }
            }

        val notGrantedPermission = mutableListOf<String>()
        MANIFEST.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notGrantedPermission.add(permission)
            }
        }

        if (notGrantedPermission.isNotEmpty()) {
            requestPermissionLauncher.launch(notGrantedPermission.toTypedArray())
        } else {
            initView()
        }

    }

    private fun initView() {
        mBinding?.recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = flexboxLayoutManager
        }
        mViewModel.readPic()

        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            mViewModel.getPicData(0)
            it.finishRefresh()
        }

        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getPicData(mViewModel.page)
            mViewModel.page += 1
            it.finishLoadMore()
        }

        mBinding?.back?.setOnClickListener {
            finish()
        }
    }

    override fun initObser() {
        super.initObser()
        mViewModel.imageUriList.observe(this) {
            adapter.dataList.clear()
            adapter.dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

}