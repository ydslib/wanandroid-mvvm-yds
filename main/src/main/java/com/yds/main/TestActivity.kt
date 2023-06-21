package com.yds.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.main.databinding.ActivityTestBinding

class TestActivity : DataBindingActivity<ActivityTestBinding, BaseViewModel>() {

    var count = 0

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_test)
    }

    override fun initData() {
        super.initData()
        mBinding?.singleTop?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("scheme://test/test/TestActivity2"))
            startActivity(intent)
//            for (i in 0..10) {
//                count++
//            }
//            mBinding?.count?.text = count.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        println("singleTop-onPause")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("singleTop-onCreate")
        if (savedInstanceState != null) {
            mBinding?.count?.text = savedInstanceState.getString("count")
        }
    }

    override fun onStart() {
        super.onStart()
        println("singleTop-onStart")
    }

    override fun onResume() {
        super.onResume()
        println("singleTop-onResume")
    }

    override fun onRestart() {
        super.onRestart()
        println("singleTop-onRestart")
    }

    override fun onStop() {
        super.onStop()
        println("singleTop-onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("singleTop-onDestroy")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("singleTop-onNewIntent")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("singleTop-onSaveInstanceState")
        outState.putString("count", mBinding?.count?.text?.toString() ?: "")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        println("singleTop-onRestoreInstanceState")
    }
}