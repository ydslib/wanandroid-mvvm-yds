package com.yds.home.db

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yds.home.model.ArticleModel
import com.yds.home.model.BannerItem
import com.yds.home.model.BaseArticle
import com.yds.home.model.HomeModel

@Database(
    entities = [HomeModel::class],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {

    private val mIsDatabaseCreated by lazy {
        MutableLiveData<Boolean>()
    }

    abstract fun articleDao(): BaseArticleDao

    companion object {
        val DATABASE_NAME = "base-article.db"

        private var sInstance: ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase? {
            if (sInstance == null) {
                synchronized(ArticleDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.applicationContext)
                        sInstance?.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance
        }


        private fun buildDatabase(context: Context): ArticleDatabase {
            return Room.databaseBuilder(context, ArticleDatabase::class.java, DATABASE_NAME).build()
        }
    }

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }


}