package com.crystallake.scan.manager

import com.crystallake.scan.ocr.OcrManager

interface IManagerFactory {
    fun createOcrManager(): OcrManager
}