package com.yds.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.crystallake.base.fragment.DataBindingFragment
import com.yds.base.bus.Bus
import com.yds.base.bus.BusChannel

abstract class BaseDataBindingFragment<D : ViewDataBinding, VM : ViewModel> :
    DataBindingFragment<D, VM>() {


    fun showLoading(loadingView: LottieAnimationView?) {
        loadingView?.let {
            it.setAnimation("loading_bus.json")
            it.repeatCount = LottieDrawable.INFINITE
            it.repeatMode = LottieDrawable.REVERSE
            it.playAnimation()
        }
    }

    fun hideLoading(loadingView: LottieAnimationView?) {
        loadingView?.pauseAnimation()
    }

    inline fun loginStateChangeObserve(crossinline block: () -> Unit) {
        Bus.observeSticky(BusChannel.LOGIN_STATUS_CHANNEL, this, Observer<Boolean> {
            block()
        })
    }

}