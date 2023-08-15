package com.crystallake.sdklog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crystallake.sdklog.util.FileUtil
import java.io.File

@Database(entities = [LogDataModel::class], version = 1)
abstract class LogRoomDatabase : RoomDatabase() {

    abstract fun logDao(): LogRoomDao

    companion object {
        const val DATABASE_NAME = "log.db"
        private var sInstance: LogRoomDatabase? = null

        fun getInstance(context: Context): LogRoomDatabase? {
            if (sInstance == null) {
                synchronized(this) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context)
                    }
                }
            }
            return sInstance
        }

        private fun buildDatabase(context: Context): LogRoomDatabase {
            val logsDir: File = FileUtil.getStorageDirect(context, "databases")
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            val file = File(logsDir, DATABASE_NAME)
            return Room.databaseBuilder(context, LogRoomDatabase::class.java, file.absolutePath).build()
        }
    }

}