package com.crystallake.sdklog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
            return Room.databaseBuilder(context, LogRoomDatabase::class.java, DATABASE_NAME).build()
        }
    }

}