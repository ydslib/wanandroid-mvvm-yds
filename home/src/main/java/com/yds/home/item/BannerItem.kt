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
import com.yds.home.model.BannerItem
import com.youth.banner.indicator.CircleIndicator

class BannerItem(val bannerItem: List<BannerItem>,private val lifecycleOwner: LifecycleOwner) : ItemProxy<HomeBannerItemBinding>() {
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
        binding.homeBanner.addBannerLifecycleObserver(lifecycleOwner)
        binding.homeBanner.setAdapter(object :BannerImageAdapter<BannerItem>(bannerItem){
            override fun onBindView(
                holder: BannerImageHolder,
                data: BannerItem?,
                position: Int,
                size: Int
            ) {
                Glide.with(binding.homeBanner.context)
                    .load(data?.imagePath)
                    .centerCrop()
                    .into(holder.imageView)

            }
        }).indicator = CircleIndicator(binding.homeBanner.context)
    }
}