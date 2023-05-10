package com.crystallake.scan.utils

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtil {
    // 获取文件目录
    @JvmStatic
    fun getStorageDirect(context: Context, type: String? = null): File {
        if (isExternalStorageEnable()) {
            // sdk > 19时，支持type获取文件夹路径
            return context.getExternalFilesDir(type) ?: File("")
        }
        return type?.let { File(context.filesDir, type) } ?: context.filesDir
    }
    /**
     * 判断外部存储是否移除了，如，低版本的手机有SD卡移除功能
     *
     * MEDIA_MOUNTED:表明sd对象是存在并具有读/写权限
     */
    private fun isExternalStorageEnable(): Boolean {
        // 判断外部存储是是否可用，
        val isStorageEnable = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        // 判断外部存储是否移除了，如，低版本的手机有SD卡移除功能
        val isStorageRemoved = Environment.isExternalStorageRemovable()
        return isStorageEnable || !isStorageRemoved
    }
}