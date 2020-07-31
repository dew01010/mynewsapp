package com.dew.newsapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.dew.newsapplication.model.HeadLineRes
import com.dew.newsapplication.api.repo.NewsRepo
import com.dew.newsapplication.model.ArticleInfo
import com.dew.newsapplication.utility.neworkutil.ApiResult
import com.msewa.healthism.util.Resources
import com.msewa.healthism.util.Status
import com.msewa.healthism_merchant.util.NetworkException

/*
* this is viewModel that works as abstraction layer between view[Activity or Fragment] and data source class[NewsRepo]*/

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    val newsLiveData: LiveData<Resources<HeadLineRes>>
        get() {
            return resources
        }
    private var resources = MutableLiveData<Resources<HeadLineRes>>()
    private var mediatorLiveDataResources = MediatorLiveData<Resources<ArrayList<ArticleInfo>>>()
    private var isTotalPageLoaded: Boolean = false
    private var currentPageSize: Int = 1
    private val repo = NewsRepo(application.applicationContext)

    fun fetchNewsHeadlines(id: String) {
        if (!isTotalPageLoaded) {
            resources.postValue(Resources.loading())
            repo.fetchNewsHeadlines(id, currentPageSize) {
                when (it) {
                    is ApiResult.Success -> {
                        if (it.data.articles.size < it.data.totalResults) {
                            isTotalPageLoaded = true
                        }
                        currentPageSize += 1
                        resources.postValue(Resources.success(it.data))
                    }
                    is ApiResult.Error -> {
                        when (it.exception) {
                            is NetworkException -> {
                                resources.postValue(Resources.noInternet())
                            }
                            else -> {
                                resources.postValue(
                                    Resources.errorMessage(
                                        it.exception.message ?: "Some thing went wrong !!"
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchHeadlines(id: String) {
        val liveData = repo.fetchNewsHeadlinesWithCache(id)
        mediatorLiveDataResources.addSource(liveData) {
            when (it.code) {
                Status.SUCCESS -> {
                    Log.v("*1-","SUCCESS: ${it.data?.size}")
                }
                Status.LOADING -> {
                    Log.v("*1-","${"LOADING"}")
                }
                Status.ERROR -> {
                    Log.v("*1-","${"ERROR"}")

                }
                Status.NO_INTERNET -> {
                    Log.v("*1-","${"NO INTERNET"}")

                }
            }

        }

    }


}