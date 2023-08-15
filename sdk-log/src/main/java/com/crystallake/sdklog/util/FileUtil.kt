package com.crystallake.sdklog.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import java.io.*


/**
 * @author reber
 * on 2022/4/21 17:32
 */
object FileUtil {

    // 根据string-path判断文件是否存在
    fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists()
    }


    private fun isFileExistsApi29(context: Context, filePath: String?): Boolean {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                val uri: Uri = Uri.parse(filePath)
                val cr: ContentResolver = context.contentResolver
                val afd = cr.openAssetFileDescriptor(uri, "r") ?: return false
                try {
                    afd.close()
                } catch (ignore: IOException) {
                }
            } catch (e: FileNotFoundException) {
                return false
            }
            return true
        }
        return false
    }


    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    @JvmStatic
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }


    // 对文件重新命名
    @JvmStatic
    fun renameFile(file: File, newFilePath: String) {
        file.renameTo(File(newFilePath))
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

    // 先考虑外部缓存存储，然后在考虑内部缓存存储
    fun getCacheStoragePath(context: Context, type: String? = null): String {
        val cachePath = if (isExternalStorageEnable()) context.externalCacheDir?.absolutePath ?: ""
        else context.cacheDir.absolutePath
        return type?.let { cachePath.plus(File.separator).plus(type) } ?: cachePath
    }

    /**
     * 先考虑外部缓存存储，然后在考虑内部缓存存储
     *
     * @param type files下的指定文件夹路径:Environment.DIRECTORY_DOCUMENTS
     */
    fun getStorageFilePath(context: Context, type: String? = null): String {
        return getStorageDirect(context, type).absolutePath
    }

    // 获取文件目录
    @JvmStatic
    fun getStorageDirect(context: Context, type: String? = null): File {
        if (isExternalStorageEnable()) {
            // sdk > 19时，支持type获取文件夹路径
            return context.getExternalFilesDir(type) ?: File("")
        }
        return type?.let { File(context.filesDir, type) } ?: context.filesDir
    }
}
