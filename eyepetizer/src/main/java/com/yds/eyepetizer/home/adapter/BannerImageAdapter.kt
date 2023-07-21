package com.yds.eyepetizer.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yds.eyepetizer.databinding.EyeBannerImageItemBinding
import com.yds.eyepetizer.home.model.HomeItem
import com.youth.banner.adapter.BannerAdapter

class BannerImageAdapter(datas: List<HomeItem>) :
    BannerAdapter<HomeItem, BannerImageAdapter.BannerViewHolder>(datas) {

    class BannerViewHolder(val binding: EyeBannerImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val binding = EyeBannerImageItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindView(holder: BannerViewHolder?, data: HomeItem?, position: Int, size: Int) {
        holder?.itemView?.context?.let {
            Glide.with(it).load(data?.data?.content?.data?.cover?.feed).into(holder.binding.ivBanner)
        }
        holder?.binding?.tips?.text = data?.data?.content?.data?.title
    }
}