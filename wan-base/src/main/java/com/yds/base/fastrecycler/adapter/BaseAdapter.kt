package com.yds.base.fastrecycler.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var debuggable = false

    fun isDebuggable(): Boolean {
        return debuggable
    }

    fun openDebug() {
        debuggable = true
    }

    val dataList: MutableList<T> by lazy {
        mutableListOf()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}