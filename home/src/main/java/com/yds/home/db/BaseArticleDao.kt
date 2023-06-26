package com.yds.home.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yds.home.model.ArticleModel
import com.yds.home.model.HomeModel

@Dao
interface BaseArticleDao {

    @Query("Select * FROM homeModel")
    suspend fun loadAllData(): HomeModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(articles: HomeModel?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticleModel(articleModel: ArticleModel?)
}