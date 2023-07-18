package com.yds.main.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.yds.main.R
import com.yds.main.databinding.ItemGalleryBinding

class GalleryAdapter(val click: (View, Int) -> Unit) :
    BaseQuickAdapter<Bitmap, BaseViewHolder>(R.layout.item_gallery) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemGalleryBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: Bitmap) {
        val binding = DataBindingUtil.getBinding<ItemGalleryBinding>(holder.itemView)
        val startTime = System.nanoTime()
        val lp = binding?.img?.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1f
        }

        binding?.img?.setImageBitmap(item)

        binding?.img?.setOnClickListener {
            click.invoke(it, holder.adapterPosition)
        }
        Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
    }
}