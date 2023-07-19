package com.yds.home.fjs.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ScreenUtils
import com.yds.home.databinding.HomeBannerItemBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.bumptech.glide.Glide
import com.yds.base.fastrecycler.ItemProxy
import com.yds.base.fastrecycler.viewholder.ItemViewHolder
import com.yds.home.R
import com.yds.home.fjs.model.BannerItemData
import com.youth.banner.indicator.CircleIndicator

class BannerItem(val bannerItem: List<BannerItemData>, private val lifecycleOwner: LifecycleOwner) :
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

    inner class MyBannerImageAdapter(val bannerItem: List<BannerItemData>) : BannerImageAdapter<BannerItemData>(bannerItem) {
        override fun onBindView(holder: BannerImageHolder?, data: BannerItemData?, position: Int, size: Int) {
            holder?.let {
                Glide.with(it.imageView.context)
                    .load(data?.imagePath)
                    .centerCrop()
                    .override(ScreenUtils.getScreenWidth(), it.imageView.context.resources.getDimensionPixelSize(R.dimen.dp_200))
                    .into(it.imageView)
            }
        }
    }
}