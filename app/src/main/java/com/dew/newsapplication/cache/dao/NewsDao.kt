package com.dew.newsapplication.cache.dao

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dew.newsapplication.model.ArticleInfo

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(list: ArrayList<ArticleInfo>)
}