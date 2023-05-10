/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crystallake.scan.ocr.crop

import android.content.ContentResolver
import android.provider.MediaStore
import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.media.ExifInterface
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.*
import java.lang.IllegalArgumentException

/*
 * Modified from original in AOSP.
 */
internal object CropUtil {
    private const val SCHEME_FILE = "file"
    private const val SCHEME_CONTENT = "content"
    fun closeSilently(c: Closeable?) {
        if (c == null) return
        try {
            c.close()
        } catch (t: Throwable) {
            // Do nothing
        }
    }

    fun getExifRotation(imageFile: File?): Int {
        return if (imageFile == null) 0 else try {
            val exif = ExifInterface(imageFile.absolutePath)
            when (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> ExifInterface.ORIENTATION_UNDEFINED
            }
        } catch (e: IOException) {
            Log.e("Error getting Exif data", e.message + "")
            0
        }
    }

    fun copyExifRotation(sourceFile: File?, destFile: File?): Boolean {
        return if (sourceFile == null || destFile == null) false else try {
            val exifSource = ExifInterface(sourceFile.absolutePath)
            val exifDest = ExifInterface(destFile.absolutePath)
            exifDest.setAttribute(
                ExifInterface.TAG_ORIENTATION,
                exifSource.getAttribute(ExifInterface.TAG_ORIENTATION)
            )
            exifDest.saveAttributes()
            true
        } catch (e: IOException) {
            Log.e("Error copying Exif data", e.message + "")
            false
        }
    }

    fun getFromMediaUri(context: Context, resolver: ContentResolver, uri: Uri?): File? {
        if (uri == null) return null
        if (SCHEME_FILE == uri.scheme) {
            return File(uri.path)
        } else if (SCHEME_CONTENT == uri.scheme) {
            val filePathColumn =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
            var cursor: Cursor? = null
            try {
                cursor = resolver.query(uri, filePathColumn, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = if (uri.toString()
                            .startsWith("content://com.google.android.gallery3d")
                    ) cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) else cursor.getColumnIndex(
                        MediaStore.MediaColumns.DATA
                    )
                    // Picasa images on API 13+
                    if (columnIndex != -1) {
                        val filePath = cursor.getString(columnIndex)
                        if (!TextUtils.isEmpty(filePath)) {
                            return File(filePath)
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                // Google Drive images
                return getFromMediaUriPfd(context, resolver, uri)
            } catch (ignored: SecurityException) {
                // Nothing we can do
            } finally {
                cursor?.close()
            }
        }
        return null
    }

    @Throws(IOException::class)
    private fun getTempFilename(context: Context): String {
        val outputDir = context.cacheDir
        val outputFile = File.createTempFile("image", "tmp", outputDir)
        return outputFile.absolutePath
    }

    private fun getFromMediaUriPfd(context: Context, resolver: ContentResolver, uri: Uri?): File? {
        if (uri == null) return null
        var input: FileInputStream? = null
        var output: FileOutputStream? = null
        try {
            val pfd = resolver.openFileDescriptor(uri, "r")
            val fd = pfd?.fileDescriptor
            input = FileInputStream(fd)
            val tempFilename = getTempFilename(context)
            output = FileOutputStream(tempFilename)
            var read: Int
            val bytes = ByteArray(4096)
            while (input.read(bytes).also { read = it } != -1) {
                output.write(bytes, 0, read)
            }
            return File(tempFilename)
        } catch (ignored: IOException) {
            // Nothing we can do
        } finally {
            closeSilently(input)
            closeSilently(output)
        }
        return null
    }

    fun startBackgroundJob(
        activity: CropImageActivity,
        title: String?, message: String?, job: Runnable, handler: Handler
    ) {
        // Make the progress dialog uncancelable, so that we can guarantee
        // the thread will be done before the activity getting destroyed
        val dialog = ProgressDialog.show(
            activity, title, message, true, false
        )
        Thread(BackgroundJob(activity, job, dialog, handler)).start()
    }

    private class BackgroundJob(
        private val activity: CropImageActivity, private val job: Runnable,
        private val dialog: ProgressDialog, handler: Handler
    ) : DefaultLifecycleObserver, Runnable {
        private val handler: Handler
        private val cleanupRunner = Runnable {
            activity.lifecycle.removeObserver(this)
            if (dialog.window != null) dialog.dismiss()
        }

        override fun run() {
            try {
                job.run()
            } finally {
                handler.post(cleanupRunner)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            cleanupRunner.run()
            handler.removeCallbacks(cleanupRunner)
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            dialog.hide()
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            dialog.show()
        }

        init {
            activity.lifecycle.addObserver(this)
            this.handler = handler
        }
    }
}