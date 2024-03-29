package com.yds.base.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

object Bus {

    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }

    /**
     * 应用进程生命周期内订阅LiveDataEventBus消息
     */
    inline fun <reified T> observeForever(channel: String, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observeForever(observer)
    }

    inline fun <reified T> observeSticky(
        channel: String,
        owner: LifecycleOwner,
        observer: Observer<T>
    ) {
        LiveEventBus.get(channel, T::class.java).observeSticky(owner, observer)
    }

    inline fun <reified T> observeStickyForever(channel: String, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observeStickyForever(observer)
    }
}