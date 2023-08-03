package com.crystallake.sdklog.db

import android.content.Context
import androidx.lifecycle.asLiveData
import com.crystallake.sdklog.IManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class LogRoomManager : IManager {

    var context: Context? = null

    var logRoomDatabase: LogRoomDatabase? = null

    override fun initManager(context: Context): IManager {
        this.context = context
        logRoomDatabase = LogRoomDatabase.getInstance(context)
        return this
    }

    override fun saveLog(logDataModel: LogDataModel) {
        GlobalScope.launch {
            logRoomDatabase?.logDao()?.insertLog(logDataModel)
        }
    }

    override fun getLog(key: String, def: String?, result: ((String?) -> Unit)?): String? {
        if (logRoomDatabase?.logDao() == null) {
            return def
        }
        var logDataModel: LogDataModel? = null
        CoroutineScope(Dispatchers.Main).launch {
            val asyncJob = async(Dispatchers.IO) {
                logRoomDatabase?.logDao()?.getLogData(key)
            }
            logDataModel = asyncJob.await()
            result?.invoke(logDataModel.toString())
        }
        return ""
    }

}