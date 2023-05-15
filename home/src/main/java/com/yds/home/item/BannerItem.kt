package com.yds.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.home.databinding.HomeBannerItemBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class BannerItem(private val lifecycleOwner: LifecycleOwner) : ItemProxy<HomeBannerItemBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HomeBannerItemBinding {
        return HomeBannerItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: HomeBannerItemBinding
    ) {
        val dataList = mutableListOf<String>(
            "https://scpic.chinaz.net/files/default/imgs/2023-05-04/bf6cf407a4b098cd.jpg",
            "https://scpic.chinaz.net/files/default/imgs/2023-05-06/8b75e20fa0bda198.jpg",
            "https://scpic.chinaz.net/files/default/imgs/2023-04-27/5202d1e005f23f95.jpg",
            "https://scpic.chinaz.net/files/default/imgs/2023-04-26/927afbe5af4fec22.jpg"
        )
        binding.homeBanner.addBannerLifecycleObserver(lifecycleOwner)
        binding.homeBanner.setAdapter(object :BannerImageAdapter<String>(dataList){
            override fun onBindView(
                holder: BannerImageHolder,
                data: String?,
                position: Int,
                size: Int
            ) {
                Glide.with(binding.homeBanner.context)
                    .load(data)
                    .centerCrop()
                    .into(holder.imageView)

            }
        })
    }



}