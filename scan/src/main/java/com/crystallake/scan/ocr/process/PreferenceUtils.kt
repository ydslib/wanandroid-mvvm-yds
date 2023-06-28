package com.crystallake.scan.ocr.process

import android.content.Context
import android.preference.PreferenceManager
import com.crystallake.scan.R

object PreferenceUtils {
    const val PREF_KEY_CAMERA_LIVE_VIEWPORT = "clv"

    fun isCameraLiveViewportEnabled(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(PREF_KEY_CAMERA_LIVE_VIEWPORT, false)
    }

    fun shouldHideDetectionInfo(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefKey = context?.getString(R.string.pref_key_info_hide)
        return sharedPreferences.getBoolean(prefKey, false)
    }
}