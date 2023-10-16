package com.crystallake.scan.ocr.live

import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.resources.RouterPath
import com.crystallake.scan.R
import com.crystallake.scan.databinding.ActivityOcrBinding
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.yds.base.BaseDataBindingActivity
import com.yds.base.GsonUtil
import java.io.IOException


@Route(path = RouterPath.SCAN_OCR_ACTIVITY)
class OcrActivity : BaseDataBindingActivity<ActivityOcrBinding, BaseViewModel>() {
    companion object {
        const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
    }

    private var cameraSource: CameraSource? = null
    private val scanResultSet by lazy {
        mutableSetOf<String>()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_ocr)
    }

    override fun initData() {
        super.initData()
        createCameraSource(TEXT_RECOGNITION_LATIN)
        startCameraSource()

        mBinding?.finish?.setOnClickListener {
            val resultSet = GsonUtil.gson.toJson(scanResultSet)
            ARouter.getInstance().build(RouterPath.SCAN_RESULT_LIST_ACTIVITY).apply {
                extras.putString("scanResult", resultSet)
            }.navigation()
        }
    }

    private fun startCameraSource() {
        if (cameraSource != null) {
            try {

                mBinding?.graphicOverlay?.let {
                    mBinding?.previewView?.start(cameraSource, it)
                }
            } catch (e: IOException) {
                Log.e("OcrActivity", "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scanResultSet.clear()
        createCameraSource(TEXT_RECOGNITION_LATIN)
        startCameraSource()
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null && mBinding?.graphicOverlay != null) {
            cameraSource = CameraSource(this, mBinding?.graphicOverlay)
        }
        try {
            when (model) {
                TEXT_RECOGNITION_LATIN -> {
                    cameraSource?.setMachineLearningFrameProcessor(
                        TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build()) {
                            val list = it.textBlocks.filter {
                                isContainDigit(it.text)
                            }
                            for (textBlock in list) {
                                val textStr = textBlock.text.replace("\n", "")
                                val res = if (textStr.contains(":")) {
                                    val index = textStr.indexOf(":")
                                    println("test:$index----${textStr}----${"Date of Manufacturer: 2022-11-14".substring(20)}")
                                    if (index != -1 && index + 1 < textStr.length) {
                                        textStr.substring(index + 1)
                                    } else {
                                        ""
                                    }
                                } else if (textStr.contains("：")) {
                                    val index = textStr.indexOf("：")
                                    println("test:$index----${textStr}----${"Date of Manufacturer: 2022-11-14".substring(20)}")
                                    if (index != -1 && index + 1 < textStr.length) {
                                        textStr.substring(index + 1)
                                    } else {
                                        ""
                                    }
                                } else {
                                    textStr
                                }
                                if (!res.isNullOrEmpty() && !res.contains(Regex("[^0-9]"))) {
                                    println("QcrActivity:$res")
                                    scanResultSet.add(res.trim())
                                }
                            }
                        }
                    )
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding?.previewView?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
    }

    private fun isContainDigit(res: String?): Boolean {
        if (res.isNullOrEmpty()) return false
        res.toCharArray().forEach {
            if (it.isDigit()) {
                return true
            }
        }
        return false
    }
}