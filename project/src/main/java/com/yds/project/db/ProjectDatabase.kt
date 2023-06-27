package com.yds.project.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yds.home.model.BaseArticle
import com.yds.project.db.dao.ProjectDao
import com.yds.project.model.ProjectTitleModel

@Database(
    entities = [
        ProjectTitleModel::class,
        BaseArticle::class
    ],
    version = 1
)
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun getProjectDao(): ProjectDao

    companion object {
        private val PROJECT_DATABASE = "project_db.db"
        private var sInstance: ProjectDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ProjectDatabase? {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(context, ProjectDatabase::class.java, PROJECT_DATABASE).build()
            }
            return sInstance
        }
    }
}