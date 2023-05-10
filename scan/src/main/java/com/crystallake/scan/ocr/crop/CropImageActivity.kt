package com.crystallake.scan.ocr.crop

import android.os.Bundle
import android.annotation.TargetApi
import android.view.WindowManager
import android.content.Intent
import android.graphics.*
import android.provider.MediaStore
import android.opengl.GLES10
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch
import com.crystallake.scan.R

class CropImageActivity : AppCompatActivity() {
    private val handler = Handler()
    private var aspectX = 0
    private var aspectY = 0

    // Output image
    private var maxX = 0
    private var maxY = 0
    private var exifRotation = 0
    private var saveAsPng = false
    private var sourceUri: Uri? = null
    private var saveUri: Uri? = null
    var isSaving = false
        private set
    private var sampleSize = 0
    private var rotateBitmap: RotateBitmap? = null
    private var imageView: CropImageView? = null
    private var cropView: HighlightView? = null
    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setupWindowFlags()
        setupViews()
        loadInput()
        if (rotateBitmap == null) {
            finish()
            return
        }
        startCrop()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setupWindowFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setupViews() {
        setContentView(R.layout.activity_crop)
        imageView = findViewById<View>(R.id.crop_image) as CropImageView
        imageView?.setRecycler(object : ImageViewTouchBase.Recycler {
            override fun recycle(b: Bitmap?) {
                b?.recycle()
                System.gc()
            }

        })
        findViewById<View>(R.id.btn_cancel).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        findViewById<View>(R.id.btn_done).setOnClickListener { onSaveClicked() }
    }

    private fun loadInput() {
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            aspectX = extras.getInt(Crop.Extra.ASPECT_X)
            aspectY = extras.getInt(Crop.Extra.ASPECT_Y)
            maxX = extras.getInt(Crop.Extra.MAX_X)
            maxY = extras.getInt(Crop.Extra.MAX_Y)
            saveAsPng = extras.getBoolean(Crop.Extra.AS_PNG, false)
            saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT)
        }
        sourceUri = intent.data

        sourceUri?.let {
            exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, contentResolver, it))
            var inputStream: InputStream? = null
            try {
                sampleSize = calculateBitmapSampleSize(it)
                inputStream = contentResolver.openInputStream(it)
                val option = BitmapFactory.Options()
                option.inSampleSize = sampleSize
                rotateBitmap = RotateBitmap(BitmapFactory.decodeStream(inputStream, null, option), exifRotation)
            } catch (e: IOException) {
                Log.e("Error reading image: " + e.message, e.message + "")
                setResultException(e)
            } catch (e: OutOfMemoryError) {
                Log.e("OOM reading image: " + e.message, e.message + "")
                setResultException(e)
            } finally {
                CropUtil.closeSilently(inputStream)
            }
        }
    }

    @Throws(IOException::class)
    private fun calculateBitmapSampleSize(bitmapUri: Uri?): Int {
        var inputStream: InputStream? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        try {
            bitmapUri?.let {
                inputStream = contentResolver.openInputStream(it)
            }
            BitmapFactory.decodeStream(inputStream, null, options) // Just get image size
        } finally {
            CropUtil.closeSilently(inputStream)
        }
        val maxSize = maxImageSize
        var sampleSize = 1
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize shl 1
        }
        return sampleSize
    }

    private val maxImageSize: Int
        private get() {
            val textureLimit = maxTextureSize
            return if (textureLimit == 0) {
                SIZE_DEFAULT
            } else {
                Math.min(textureLimit, SIZE_LIMIT)
            }
        }

    // The OpenGL texture size is the maximum size that can be drawn in an ImageView
    private val maxTextureSize: Int
        private get() {
            // The OpenGL texture size is the maximum size that can be drawn in an ImageView
            val maxSize = IntArray(1)
            GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0)
            return maxSize[0]
        }

    private fun startCrop() {
        if (isFinishing) {
            return
        }
        imageView?.setImageRotateBitmapResetBase(rotateBitmap, true)
        CropUtil.startBackgroundJob(
            this, null, resources.getString(R.string.loading),
            {
                val latch = CountDownLatch(1)
                handler.post {
                    if (imageView?.scale == 1f) {
                        imageView?.center()
                    }
                    latch.countDown()
                }
                try {
                    latch.await()
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
                Cropper().crop()
            }, handler
        )
    }

    private inner class Cropper {
        private fun makeDefault() {
            if (rotateBitmap == null) {
                return
            }
            val hv = HighlightView(imageView)
            val width = rotateBitmap?.width ?: 0
            val height = rotateBitmap?.height ?: 0
            val imageRect = Rect(0, 0, width, height)

            // Make the default size about 4/5 of the width or height
            var cropWidth = Math.min(width, height) * 4 / 5
            var cropHeight = cropWidth
            if (aspectX != 0 && aspectY != 0) {
                if (aspectX > aspectY) {
                    cropHeight = cropWidth * aspectY / aspectX
                } else {
                    cropWidth = cropHeight * aspectX / aspectY
                }
            }
            val x = (width - cropWidth) / 2
            val y = (height - cropHeight) / 2
            val cropRect = RectF(x.toFloat(), y.toFloat(), (x + cropWidth).toFloat(), (y + cropHeight).toFloat())
            hv.setup(imageView?.unrotatedMatrix, imageRect, cropRect, aspectX != 0 && aspectY != 0)
            imageView?.add(hv)
        }

        fun crop() {
            handler.post {
                makeDefault()
                imageView?.invalidate()
                if (imageView?.highlightViews?.size == 1) {
                    cropView = imageView?.highlightViews!![0]
                    cropView?.setFocus(true)
                }
            }
        }
    }

    private fun onSaveClicked() {
        if (cropView == null || isSaving) {
            return
        }
        isSaving = true
        val croppedImage: Bitmap?
        val r = cropView?.getScaledCropRect(sampleSize.toFloat())
        val width = r?.width() ?: 0
        val height = r?.height() ?: 0
        var outWidth = width
        var outHeight = height
        if (maxX > 0 && maxY > 0 && (width > maxX || height > maxY)) {
            val ratio = width.toFloat() / height.toFloat()
            if (maxX.toFloat() / maxY.toFloat() > ratio) {
                outHeight = maxY
                outWidth = (maxY.toFloat() * ratio + .5f).toInt()
            } else {
                outWidth = maxX
                outHeight = (maxX.toFloat() / ratio + .5f).toInt()
            }
        }
        croppedImage = try {
            decodeRegionCrop(r, outWidth, outHeight)
        } catch (e: IllegalArgumentException) {
            setResultException(e)
            finish()
            return
        }
        if (croppedImage != null) {
            imageView?.setImageRotateBitmapResetBase(RotateBitmap(croppedImage, exifRotation), true)
            imageView?.center()
            imageView?.highlightViews?.clear()
        }
        saveImage(croppedImage)
    }

    private fun saveImage(croppedImage: Bitmap?) {
        if (croppedImage != null) {
            val b: Bitmap = croppedImage
            CropUtil.startBackgroundJob(
                this, null, resources.getString(R.string.crop_saving),
                { saveOutput(b) }, handler
            )
        } else {
            finish()
        }
    }

    private fun decodeRegionCrop(rect: Rect?, outWidth: Int, outHeight: Int): Bitmap? {
        // Release memory now
        var rect = rect
        clearImageView()
        var inputStream: InputStream? = null
        var croppedImage: Bitmap? = null
        try {
            sourceUri?.let {
                inputStream = contentResolver.openInputStream(it)
            }

            var decoder: BitmapRegionDecoder? = null
            inputStream?.let {
                decoder = BitmapRegionDecoder.newInstance(it, false)
            }

            val width = decoder?.width ?: 0
            val height = decoder?.height ?: 0
            if (exifRotation != 0) {
                // Adjust crop area to account for image rotation
                val matrix = Matrix()
                matrix.setRotate(-exifRotation.toFloat())
                val adjusted = RectF()
                matrix.mapRect(adjusted, RectF(rect))

                // Adjust to account for origin at 0,0
                adjusted.offset(if (adjusted.left < 0) width.toFloat() else 0f, if (adjusted.top < 0) height.toFloat() else 0f)
                rect = Rect(adjusted.left.toInt(), adjusted.top.toInt(), adjusted.right.toInt(), adjusted.bottom.toInt())
            }
            try {
                croppedImage = decoder?.decodeRegion(rect, BitmapFactory.Options())
                if (croppedImage != null && ((rect?.width()?:0) > outWidth || (rect?.height()?:0) > outHeight)) {
                    val matrix = Matrix()
                    matrix.postScale(outWidth.toFloat() / (rect?.width()?:1), outHeight.toFloat() / (rect?.height()?:1))
                    croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.width, croppedImage.height, matrix, true)
                }
            } catch (e: IllegalArgumentException) {
                // Rethrow with some extra information
                throw IllegalArgumentException(
                    "Rectangle " + rect + " is outside of the image ("
                            + width + "," + height + "," + exifRotation + ")", e
                )
            }
        } catch (e: IOException) {
            Log.e("Error cropping image: " + e.message, e.message+"")
            setResultException(e)
        } catch (e: OutOfMemoryError) {
            Log.e("OOM cropping image: " + e.message, e.message+"")
            setResultException(e)
        } finally {
            CropUtil.closeSilently(inputStream)
        }
        return croppedImage
    }

    private fun clearImageView() {
        imageView?.clear()
        rotateBitmap?.recycle()
        System.gc()
    }

    private fun saveOutput(croppedImage: Bitmap) {
        if (saveUri != null) {
            var outputStream: OutputStream? = null
            try {
                saveUri?.let {
                    outputStream = contentResolver.openOutputStream(it)
                }
                outputStream?.let {
                    croppedImage.compress(
                        if (saveAsPng) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
                        90,  // note: quality is ignored when using PNG
                        it
                    )
                }
            } catch (e: IOException) {
                setResultException(e)
                Log.e("Cannot open file: $saveUri", e.message+"")
            } finally {
                CropUtil.closeSilently(outputStream)
            }
            CropUtil.copyExifRotation(
                CropUtil.getFromMediaUri(this, contentResolver, sourceUri),
                CropUtil.getFromMediaUri(this, contentResolver, saveUri)
            )
            setResultUri(saveUri)
        }
        handler.post {
            imageView?.clear()
            croppedImage.recycle()
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        rotateBitmap?.recycle()
    }

    override fun onSearchRequested(): Boolean {
        return false
    }

    private fun setResultUri(uri: Uri?) {
        setResult(RESULT_OK, Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri))
    }

    private fun setResultException(throwable: Throwable) {
        setResult(Crop.RESULT_ERROR, Intent().putExtra(Crop.Extra.ERROR, throwable))
    }

    companion object {
        private const val SIZE_DEFAULT = 2048
        private const val SIZE_LIMIT = 4096
    }
}