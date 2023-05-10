package com.crystallake.scan.ocr.process

interface TextRecognizeListener<T> {
    fun onResult(result: RecognizeResult<T>)
}