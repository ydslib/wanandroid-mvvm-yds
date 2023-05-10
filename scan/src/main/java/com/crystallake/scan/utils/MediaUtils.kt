package com.crystallake.scan.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

object MediaUtils {

    fun hasCamera(context: Context): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        return intent.resolveActivity(context.packageManager) != null
    }

    fun takePhoto(context: Context, requestCode: Int, authority: String): Uri? {

        var photoURI: Uri? = null
        if (!hasCamera(context)) {
            return null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            photoURI = createImageUriAfter10(context)
        } else {
            try {
                val photoFile = createImageFileBefore10(context)
                photoURI = providerUriBefore10(
                    context,
                    photoFile,
                    authority
                )
            } catch (e: IOException) {

            }
        }
        photoURI?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, it)
            intent.also { takePictureIntent ->
                //resolveActivity如果为null，不让调用startActivityForResult，否则会崩溃，
                //https://developer.android.com/about/versions/11/privacy/package-visibility?hl=zh-cn
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    (context as? Activity)?.startActivityForResult(
                        takePictureIntent,
                        requestCode
                    )
                }
            }
        }
        return photoURI
    }

    /**
     * Android 10以下，创建保存图片的文件
     */
    @Throws(IOException::class)
    fun createImageFileBefore10(context: Context): File {
        val imageName = DateUtil.formatDate2(null);
        val storageDir = FileUtil.getStorageDirect(context, Environment.DIRECTORY_PICTURES)
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        val tempFile = File(storageDir, imageName);
        return tempFile
    }

    // 获取android10以下的Uri
    fun providerUriBefore10(context: Context, file: File, authority: String): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //适配Android 7.0以上，通过FileProvider创建一个content类型的Uri
            FileProvider.getUriForFile(context, authority, file);
        } else {
            Uri.fromFile(file);
        }
    }

    /**
     * Android 10及以上 创建图片地址uri,用于保存拍照后的照片
     */
    fun createImageUriAfter10(context: Context): Uri? {
        val status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            );
        } else {
            return context.contentResolver.insert(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                ContentValues()
            );
        }
    }
}