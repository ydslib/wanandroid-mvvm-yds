package com.yds.home.diff

import androidx.recyclerview.widget.DiffUtil
import com.yds.home.model.BaseArticle

class DiffCallBack : DiffUtil.Callback() {

    private var newDataList: MutableList<BaseArticle>? = null
    private var oldDataList: MutableList<BaseArticle>? = null


    override fun getOldListSize(): Int {
        return oldDataList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newDataList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (newDataList.isNullOrEmpty() || oldDataList.isNullOrEmpty()) {
            return false
        }
        return newDataList!![newItemPosition].id == oldDataList!![oldItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (newDataList.isNullOrEmpty() || oldDataList.isNullOrEmpty()) {
            return false
        }
        val oldData = oldDataList!![oldItemPosition]
        val newData = newDataList!![newItemPosition]
        if (oldData.author != newData.author) {
            return false
        }
        if (oldData.superChapterName != newData.superChapterName) {
            return false
        }
        if (oldData.chapterName != newData.chapterName) {
            return false
        }
        if (oldData.title != newData.title) {
            return false
        }
        if (oldData.collect != newData.collect) {
            return false
        }
        if (oldData.niceShareDate != newData.niceShareDate) {
            return false
        }
        return true
    }
}