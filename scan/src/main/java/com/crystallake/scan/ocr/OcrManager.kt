package com.crystallake.scan.ocr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.crystallake.scan.manager.IManager
import com.crystallake.scan.manager.OnActivityResult
import com.crystallake.scan.ocr.crop.CropImageActivity
import com.crystallake.scan.ocr.live.VisionImageProcessor
import com.crystallake.scan.ocr.process.RecognizeResult
import com.crystallake.scan.ocr.process.TextRecognitionProcessor
import com.crystallake.scan.ocr.process.TextRecognizeListener
import com.crystallake.scan.utils.MediaUtils
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class OcrManager : IManager<String>, DefaultLifecycleObserver {

    companion object {
        const val TEXT_RECOGNIZE_TAKE_PICTURE = 0x0010001
        const val TEXT_RECOGNIZE_CROP_PICTURE = 0x0010002
        const val FILE_PROVIDER_NAME = ".image_provider"
    }

    var mPhotoUri: Uri? = null
    var imageProcessor: VisionImageProcessor? = null
    var onActivityResult: OnActivityResult<String>? = null

    override fun onActivityResult(
        context: Context,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == TEXT_RECOGNIZE_TAKE_PICTURE) {
            val intent = Intent(context, CropImageActivity::class.java)
            intent.data = mPhotoUri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            (context as? Activity)?.startActivityForResult(intent, TEXT_RECOGNIZE_CROP_PICTURE)
        } else if (requestCode == TEXT_RECOGNIZE_CROP_PICTURE) {
            if (data != null) {
                val cropPhotoUri = data.getParcelableExtra<Uri>(MediaStore.EXTRA_OUTPUT)
                mPhotoUri = cropPhotoUri
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, cropPhotoUri)
                    createImageProcessor(context)
                    tryReloadAndDetectInImage(context, bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun tryReloadAndDetectInImage(
        context: Context,
        imageBitmap: Bitmap?
    ) {
        if (imageBitmap == null) {
            return
        }
//        imageProcessor?.processBitmap(imageBitmap, context)
    }

    override fun takePhoto(context: Context) {
        mPhotoUri = MediaUtils.takePhoto(
            context,
            TEXT_RECOGNIZE_TAKE_PICTURE,
            context.packageName + FILE_PROVIDER_NAME
        )
    }

    override fun getPhotoUri(): Uri? {
        return mPhotoUri
    }


    private fun createImageProcessor(context: Context) {
        try {
            imageProcessor?.stop()
            imageProcessor =
                TextRecognitionProcessor(
                    context,
                    TextRecognizerOptions.Builder().build(),
                    object : TextRecognizeListener<Text> {
                        override fun onResult(result: RecognizeResult<Text>) {
                            if (result is RecognizeResult.Success) {
                                val text = result.text.text
                                onActivityResult?.onActivityResult(text)
                                println("text:$text")
                            } else {

                            }
                        }
                    })
        } catch (e: Exception) {

        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        imageProcessor?.stop()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        imageProcessor?.stop()
    }

    override fun setOnResultListener(t: OnActivityResult<String>?) {
        onActivityResult = t
    }
}