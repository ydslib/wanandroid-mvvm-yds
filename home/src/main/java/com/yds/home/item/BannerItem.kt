package com.yds.home.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.yds.home.databinding.HomeBannerItemBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.bumptech.glide.Glide
import com.yds.base.fastrecycler.ItemProxy
import com.yds.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.home.model.BannerItem
import com.youth.banner.indicator.CircleIndicator

class BannerItem(val bannerItem: List<BannerItem>, private val lifecycleOwner: LifecycleOwner) :
    ItemProxy<HomeBannerItemBinding>() {
    override fun generateItemViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): HomeBannerItemBinding {
        return HomeBannerItemBinding.inflate(inflater, parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup): ItemViewHolder {
        val binding = generateItemViewBinding(LayoutInflater.from(parent.context), parent)
        val holder = BannerViewHolder(binding)
        holder.initView()
        return holder
    }

    override fun onBindItemViewHolder(
        holder: ItemViewHolder,
        position: Int,
        binding: HomeBannerItemBinding
    ) {

    }

    inner class BannerViewHolder(private val bannerItemBinding: HomeBannerItemBinding) : ItemViewHolder(bannerItemBinding) {

        override fun initView() {
            super.initView()
            bannerItemBinding.homeBanner.addBannerLifecycleObserver(lifecycleOwner)
            bannerItemBinding.homeBanner.setAdapter(MyBannerImageAdapter(bannerItem))
                .indicator = CircleIndicator(bannerItemBinding.homeBanner.context)
        }
    }

    inner class MyBannerImageAdapter(val bannerItem: List<BannerItem>) : BannerImageAdapter<BannerItem>(bannerItem) {
        override fun onBindView(holder: BannerImageHolder?, data: BannerItem?, position: Int, size: Int) {
            holder?.let {
                Glide.with(it.imageView.context)
                    .load(data?.imagePath)
                    .centerCrop()
                    .into(it.imageView)
            }
        }
    }
}