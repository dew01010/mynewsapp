package com.dew.newsapplication.cache

import android.content.Context
import androidx.room.*
import com.dew.newsapplication.cache.dao.NewsDao
import com.dew.newsapplication.model.ArticleInfo

@Database(entities = [ArticleInfo::class],version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase:RoomDatabase() {

    companion object{

        private val DB_NAME="my_news_db"
        @Volatile
        private var instance:AppDatabase?=null
        private val LOCK = Any()

        operator  fun  invoke(context: Context)= instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also {
                instance=it
            }
        }
        private fun buildDatabase(context: Context)=Room.databaseBuilder(context,AppDatabase::class.java, DB_NAME).build()

    }

    abstract fun getNewsDao():NewsDao
}