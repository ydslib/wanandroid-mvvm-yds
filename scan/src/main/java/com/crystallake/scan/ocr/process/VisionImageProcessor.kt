package com.crystallake.scan.ocr.process

import android.content.Context
import android.graphics.Bitmap

interface VisionImageProcessor {
    fun processBitmap(bitmap: Bitmap, context: Context)
    fun stop()
}