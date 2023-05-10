package com.crystallake.scan.ocr.process

sealed class RecognizeResult<out T> {
    data class Success<out T>(val text: T) : RecognizeResult<T>()

    data class Failure(val throwable: Throwable) : RecognizeResult<Nothing>()
}
