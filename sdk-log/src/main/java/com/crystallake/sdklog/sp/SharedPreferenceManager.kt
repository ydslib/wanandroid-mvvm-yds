package com.crystallake.sdklog.sp

import android.content.Context
import android.content.SharedPreferences
import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.db.LogDataModel

class SharedPreferenceManager : IManager {

    companion object {
        const val DEFAULT_PATH = "sharedLog"
    }

    private var sharedPreferences: SharedPreferences? = null

    override fun initManager(context: Context): IManager {
        sharedPreferences = context.getSharedPreferences(DEFAULT_PATH, Context.MODE_PRIVATE)
        return this
    }

    override fun saveLog(logDataModel: LogDataModel) {
        val editor = sharedPreferences?.edit()
        editor?.putString(logDataModel.key, logDataModel.toString())?.commit()
    }

    override fun getLog(key: String, def: String?, result: ((String?) -> Unit)?): String? {
        return sharedPreferences?.getString(key, def)
    }
}