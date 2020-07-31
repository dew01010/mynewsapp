package com.dew.newsapplication.api.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dew.newsapplication.cache.AppDatabase
import com.dew.newsapplication.cache.dao.NewsDao
import com.dew.newsapplication.model.ArticleInfo
import com.dew.newsapplication.model.HeadLineRes
import com.dew.newsapplication.network.apiservice.ApiConstant
import com.dew.newsapplication.network.apiservice.ApiFactory
import com.dew.newsapplication.network.apiservice.ApiRequest
import com.dew.newsapplication.utility.neworkutil.ApiResponse
import com.dew.newsapplication.utility.neworkutil.ApiResult
import com.dew.newsapplication.utility.neworkutil.MyExecutor
import com.dew.newsapplication.utility.neworkutil.NetworkResourceBound
import com.msewa.healthism.util.Resources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepo(context: Context) {

    private val newDao:NewsDao=AppDatabase.invoke(context =context ).getNewsDao()
    fun fetchNewsHeadlines(
        id: String,
        page: Int,
        listener: (result: ApiResult<HeadLineRes>) -> Unit
    ) {
        val api = ApiRequest
        api.getTopHeadlinesBySource(
            id,
            page
        ) { res: HeadLineRes?, resultCode: String?, e: Exception? ->
            when (resultCode) {
                ApiConstant.CODE_SUCCESS -> {
                    try {
                        listener(ApiResult.Success(res!!))
                    } catch (e: Exception) {
                        listener(ApiResult.Error(e))
                    }
                }
                ApiConstant.CODE_FAILED -> {
                    listener(ApiResult.Error(e!!))
                }
            }
        }
    }

    fun fetchNewsHeadlinesWithCache(
        id: String
    ): LiveData<Resources<ArrayList<ArticleInfo>>> {
        return  object : NetworkResourceBound<ArrayList<ArticleInfo>, HeadLineRes>(MyExecutor) {

            override fun loadDatafromDb(): LiveData<ArrayList<ArticleInfo>> {
                return newDao.loadAllArticle()
            }

            override fun shouldFetch(data: ArrayList<ArticleInfo>): Boolean {
                return true
            }

            override fun saveCallResult(data: HeadLineRes) {

                if(data.articles!=null){
                    for((index:Int,rowId:Long) in newDao.insertNews(data.articles).withIndex()){
                        Log.v("*1--","${rowId}")
                        if(rowId == -1L){
                            newDao.updateNews(
                                data.articles[index].id,
                                data.articles[index].author,
                                data.articles[index].title,
                                data.articles[index].url,
                                data.articles[index].urlToImage,
                                data.articles[index].publishedAt,
                                data.articles[index].content)
                        }
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<HeadLineRes>> {
                val liveData = MutableLiveData<ApiResponse<HeadLineRes>>()
                val call = ApiFactory.getApi().getTopHeadLinesByCountry(id, 100)
                val apiResponse = ApiResponse<HeadLineRes>()
                call.enqueue(object : Callback<HeadLineRes> {
                    override fun onFailure(call: Call<HeadLineRes>, t: Throwable) {
                        liveData.postValue(apiResponse.create(t))
                    }
                    override fun onResponse(call: Call<HeadLineRes>,response: Response<HeadLineRes>) {
                       liveData.postValue(apiResponse.create(response))
                    }
                })
                return liveData
            }

        }.getAsLiveData()

    }
}