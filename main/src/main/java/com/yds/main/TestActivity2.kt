package com.yds.main

import android.content.Intent
import android.os.Bundle
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.yds.main.databinding.ActivityTest2Binding

class TestActivity2 : DataBindingActivity<ActivityTest2Binding, BaseViewModel>() {


    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_test2)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("TestActivity2-onCreate")
        mBinding?.btn?.setOnClickListener {
            val intent = Intent(this@TestActivity2, TestActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        println("TestActivity2-onStart")
    }

    override fun onRestart() {
        super.onRestart()
        println("TestActivity2-onRestart")
    }

    override fun onResume() {
        super.onResume()
        println("TestActivity2-onResume")
    }

    override fun onPause() {
        super.onPause()
        println("TestActivity2-onPause")
    }

    override fun onStop() {
        super.onStop()
        println("TestActivity2-onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("TestActivity2-onDestroy")
    }

}