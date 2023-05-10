package com.crystallake.scan.manager

import android.content.Context
import android.content.Intent
import android.net.Uri

interface IManager<T> {
    fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, data: Intent?)
    fun takePhoto(context: Context)
    fun getPhotoUri(): Uri?
    fun setOnResultListener(t: OnActivityResult<T>?)
}