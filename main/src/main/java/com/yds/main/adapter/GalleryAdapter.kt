package com.yds.main.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.GalleryActivity
import com.yds.main.MainActivity
import com.yds.main.databinding.ItemGalleryBinding
import java.util.concurrent.atomic.AtomicBoolean

class GalleryAdapter(
    private val callback: () -> Unit,
    private val clickListener: (position: Int, view: View) -> Unit
) :
    SingleDataBindingAdapter<Uri, ItemGalleryBinding>() {

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
        val lp = binding.img.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1f
        }

        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(dataList[position].path, option)

        val width = option.outWidth
        option.inSampleSize = initSampleSize(width)
        option.inJustDecodeBounds = false
        option.inPreferredConfig = Bitmap.Config.ARGB_8888
        option.inMutable = true
        option.inBitmap = bitmap
        bitmap = BitmapFactory.decodeFile(dataList[position].path, option)

        binding.img.setImageBitmap(bitmap)
        binding.img.transitionName = dataList[position].path

        binding.img.setOnClickListener {
//            clickListener.invoke(position, it)
        }

        if (GalleryActivity.currentPosition != position) {
            return
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return
        }
        callback.invoke()
    }

    private fun initSampleSize(width: Int): Int {
        var tempWidth = width
        var inSampleSize = 1
        while (tempWidth > 250) {
            inSampleSize *= 2
            tempWidth = (tempWidth / inSampleSize)
        }
        println("width:$tempWidth")
        return inSampleSize
    }
}