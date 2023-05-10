package com.crystallake.scan.ocr.process

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface
import java.lang.Exception

class TextRecognitionProcessor(
    val context: Context,
    textRecognizerOptions: TextRecognizerOptionsInterface,
    val listener: TextRecognizeListener<Text>?
) : VisionProcessorBase<Text>(context) {


    val textRecognizer: TextRecognizer

    init {
        textRecognizer = TextRecognition.getClient(textRecognizerOptions)
    }

    override fun stop() {
        super.stop()
        textRecognizer.close()
    }


    override fun onSuccess(results: Text) {
        listener?.onResult(RecognizeResult.Success(results))
    }

    override fun onFailure(e: Exception) {
        listener?.onResult(RecognizeResult.Failure(e))
    }

    override fun detectInImage(image: InputImage?): Task<Text>? {
        return if (image != null) {
            textRecognizer.process(image)
        } else null
    }
}