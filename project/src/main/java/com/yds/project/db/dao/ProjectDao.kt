package com.yds.project.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.yds.home.fjs.model.BaseArticle
import com.yds.project.model.ProjectTitleModel

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectTitleModel(projectTitleModel: ProjectTitleModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaseArticle(baseArticle: BaseArticle)
}