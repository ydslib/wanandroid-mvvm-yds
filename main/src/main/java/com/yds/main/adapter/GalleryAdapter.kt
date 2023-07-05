package com.yds.main.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.BuildConfig
import com.yds.main.GalleryActivity
import com.yds.main.databinding.ItemGalleryBinding
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.pow

class GalleryAdapter(
    private val callback: () -> Unit,
    private val clickListener: (position: Int, view: View) -> Unit
) :
    SingleDataBindingAdapter<Bitmap, ItemGalleryBinding>() {

    init {
        if (BuildConfig.DEBUG) {
            openDebug()
        }
    }

    private val enterTransitionStarted by lazy {
        AtomicBoolean()
    }

    override fun generateDataBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemGalleryBinding {
        return ItemGalleryBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(binding: ItemGalleryBinding, position: Int) {
        val startTime = System.nanoTime()
        val lp = binding.img.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1f
        }
//
//        val option = BitmapFactory.Options()
//        option.inJustDecodeBounds = true
//        var bitmap = BitmapFactory.decodeFile(dataList[position].path, option)
//
//        initSampleSize(option)
//        println("width:${option.outWidth},height:${option.outHeight}")
//        option.inJustDecodeBounds = false
//        option.inPreferredConfig = Bitmap.Config.RGB_565
//        option.inMutable = true
//        option.inBitmap = bitmap
//        bitmap = BitmapFactory.decodeFile(dataList[position].path, option)
//
//        println("大小：${getAllocationByteCountWithM(bitmap)}----${bitmap.allocationByteCount}")
//
//        binding.img.setImageBitmap(bitmap)
//        Glide.with(binding.img.context).load(dataList[position]).into(binding.img)
        binding.img.setImageBitmap(dataList[position])
        binding.img.transitionName = dataList[position].toString()

        binding.img.setOnClickListener {
            clickListener.invoke(position, it)
        }

        if (GalleryActivity.currentPosition != position) {
            return
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return
        }
        callback.invoke()
        Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
    }

    private fun initSampleSize(options: BitmapFactory.Options) {
        var i = 0
        while (options.outWidth shr i >= 360 && options.outHeight shr i >= 360) {
            i++
        }
        println("${options.outWidth shr i}----$i")
        options.inSampleSize = 2.0.pow(i.toDouble()).toInt()
    }

    private fun getAllocationByteCountWithM(bitmap: Bitmap): Double {
        return (bitmap.allocationByteCount / (1024 * 1024)).toDouble()
    }
}