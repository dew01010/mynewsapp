package com.dew.newsapplication.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dew.newsapplication.model.ArticleInfo

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(list: ArrayList<ArticleInfo>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(articleInfo: ArticleInfo)

    @Query("UPDATE article SET author = :author, title = :title, url =:url,urlToImage= :urlToImage,publishedAt=:publishedAt, content=:content WHERE id=:id ")
    fun updateNews(
        id: Int,
        author: String?,
        title: String?,
        url: String?,
        urlToImage: String?,
        publishedAt: String?,
        content: String?
    )

    @Query("SELECT * FROM article")
    fun loadAllArticle(): LiveData<ArrayList<ArticleInfo>>



}