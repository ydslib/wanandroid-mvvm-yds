package com.yds.main.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.databinding.ItemGalleryBinding

class GalleryAdapter : SingleDataBindingAdapter<Uri, ItemGalleryBinding>() {
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