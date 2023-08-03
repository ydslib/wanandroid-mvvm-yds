package com.crystallake.sdklog.mmkv

import android.content.Context
import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.db.LogDataModel
import com.tencent.mmkv.MMKV

class MMKVManager : IManager {

    val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    override fun initManager(context: Context): IManager {
        MMKV.initialize(context)
        return this
    }

    override fun saveLog(logDataModel: LogDataModel) {
        mmkv.encode(logDataModel.key, logDataModel.toString())
    }

    override fun getLog(key: String, def: String?, result: ((String?) -> Unit)?): String? {
        return mmkv.decodeString(key, def)
    }
}