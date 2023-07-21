package com.yds.eyepetizer.home.item

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yds.eyepetizer.R
import com.yds.eyepetizer.databinding.EyeBannerItemBinding
import com.yds.eyepetizer.home.adapter.BannerImageAdapter
import com.yds.eyepetizer.home.model.ProviderMultiModel
import com.youth.banner.indicator.CircleIndicator

class BannerItem(val owner: LifecycleOwner) : BaseItemProvider<ProviderMultiModel>() {
    override val itemViewType: Int
        get() = ProviderMultiModel.Type.TYPE_BANNER
    override val layoutId: Int
        get() = R.layout.eye_banner_item

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onViewHolderCreated(viewHolder, viewType)
        DataBindingUtil.bind<EyeBannerItemBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: ProviderMultiModel) {
        val binding = DataBindingUtil.getBinding<EyeBannerItemBinding>(helper.itemView)
        binding?.banner?.addBannerLifecycleObserver(owner)
        binding?.banner?.setAdapter(BannerImageAdapter(item.items))
        binding?.banner?.setIndicator(CircleIndicator(binding.banner.context))
    }
}