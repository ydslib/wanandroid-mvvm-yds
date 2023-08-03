package com.crystallake

import android.content.Context
import com.crystallake.sdklog.IManager
import com.crystallake.sdklog.db.LogRoomFactory
import com.crystallake.sdklog.mmkv.MMKVFactory
import com.crystallake.sdklog.sp.SpFractory

object LogManager {

    const val SP = "SP"
    const val ROOM = "Room"
    const val MMKV = "MMKV"

    fun getManager(context: Context, type: String): IManager {
        when (type) {
            SP -> return SpFractory().createManager().initManager(context)
            ROOM -> return LogRoomFactory().createManager().initManager(context)
            MMKV -> return MMKVFactory().createManager().initManager(context)
        }
        return MMKVFactory().createManager().initManager(context)
    }

}