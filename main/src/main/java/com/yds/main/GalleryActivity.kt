package com.yds.main

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.resources.RouterPath
import com.yds.base.BaseDataBindingActivity
import com.yds.main.databinding.ActivityGalleryBinding
import com.yds.main.vm.GalleryViewModel


@Route(path = RouterPath.MAIN_GALLERY_ACTIVITY)
class GalleryActivity : BaseDataBindingActivity<ActivityGalleryBinding, GalleryViewModel>() {

    companion object {
        val MANIFEST = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
        var currentPosition = 0
        const val KEY_CURRENT_POSITION = "com.yds.main.GalleryActivity.currentPosition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION, 0)
        }
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_gallery)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(KEY_CURRENT_POSITION, currentPosition)
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
                    loadFragment()
                } else {
                    Toast.makeText(this, "未申请权限", Toast.LENGTH_SHORT).show()
                    finish()
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
            loadFragment()
        }

    }

    fun loadFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, GalleryFragment(), GalleryFragment::class.java.simpleName)
            .commit()
    }

}