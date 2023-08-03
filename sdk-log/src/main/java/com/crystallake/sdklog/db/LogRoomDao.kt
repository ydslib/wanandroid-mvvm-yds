package com.crystallake.sdklog.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(logDataModel: LogDataModel)

    @Query("select * from logDataModel where key = :k")
    suspend fun getLogData(k:String): LogDataModel
}