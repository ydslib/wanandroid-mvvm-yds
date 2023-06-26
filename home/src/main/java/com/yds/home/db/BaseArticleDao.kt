package com.yds.home.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yds.home.model.ArticleModel
import com.yds.home.model.BannerItem
import com.yds.home.model.BaseArticle
import com.yds.home.model.HomeModel

@Dao
interface BaseArticleDao {

    @Query("Select * FROM homeModel")
    suspend fun loadAllData(): HomeModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: HomeModel?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleModel(articleModel: ArticleModel?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaseArticle(baseArticle: BaseArticle?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBannerItem(bannerItem: BannerItem?)
}