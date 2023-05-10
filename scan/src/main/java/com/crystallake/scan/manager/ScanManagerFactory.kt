package com.crystallake.scan.manager

import com.crystallake.scan.ocr.OcrManager

class ScanManagerFactory : IManagerFactory {

    override fun createOcrManager(): OcrManager {
        return OcrManager()
    }

}