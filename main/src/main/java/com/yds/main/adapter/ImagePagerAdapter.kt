package com.yds.main.adapter

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.yds.main.ImageFragment

class ImagePagerAdapter(val dataList: List<Uri>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(dataList[position].path ?: "")
    }

}