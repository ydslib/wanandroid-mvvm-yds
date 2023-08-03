package com.crystallake.sdklog

import android.content.Context
import com.crystallake.sdklog.db.LogDataModel

interface IManager {

    fun initManager(context: Context): IManager

    fun saveLog(logDataModel: LogDataModel)

    fun getLog(key: String, def: String? = null, result: ((String?) -> Unit)? = null): String?
}