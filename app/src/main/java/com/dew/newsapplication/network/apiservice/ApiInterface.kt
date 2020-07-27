package com.dew.newsapplication.network.apiservice

import com.dew.newsapplication.BuildConfig
import com.dew.newsapplication.model.HeadLineRes
import com.dew.newsapplication.utility.PAGE_LIMIT
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("${ApiUrl.HEADLINE}?&apiKey=${BuildConfig.NEWS_API_KEY}&pageSize=${PAGE_LIMIT}")
    fun getTopHeadLinesByCountry(@Query("sources") source:String,@Query("page") page:Int): Call<HeadLineRes>
}