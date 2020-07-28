package com.dew.newsapplication.utility.neworkutil

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.msewa.healthism.util.Resources
import org.jetbrains.annotations.NotNull

abstract class NetworkResourceBound<CacheObject, RequestObject>(private val executor: MyExecutor) {
    private val mediatorLiveDataResult = MediatorLiveData<Resources<CacheObject>>()

    companion object {
        private const val TAG = "NetworkResourceBound"
    }

    init {
        mediatorLiveDataResult.value = Resources.loading()
        val localDbData = loadDatafromDb()

        mediatorLiveDataResult.addSource(localDbData, Observer {

            mediatorLiveDataResult.removeSource(localDbData)

            if (shouldFetch(it)) {
                // fetch from network
                fetchFromNetwork(localDbData)

            } else {
                mediatorLiveDataResult.addSource(localDbData, Observer {
                    setValues(Resources.success(it))
                })
            }
        })

    }


    private fun fetchFromNetwork(localDbSource: LiveData<CacheObject>) {
        mediatorLiveDataResult.addSource(localDbSource, Observer {
            setValues(Resources.loading(it))
        })

        val apiResponse:LiveData<ApiResponse<RequestObject>> = MutableLiveData()
        mediatorLiveDataResult.addSource(apiResponse, Observer {
            mediatorLiveDataResult.removeSource(localDbSource)
            mediatorLiveDataResult.removeSource(apiResponse)

            when(it){
                is ApiResponse.ApiSuccessResponse->{

                }
                is ApiResponse.ApiErrorResponse->{

                }
                is ApiResponse.ApiEmptyResponse->{

                }
            }
        })

    }

    private fun setValues(value: Resources<CacheObject>) {
        if (mediatorLiveDataResult.value != value) {
            mediatorLiveDataResult.value = value
        }

    }

    @NotNull
    @MainThread
    protected abstract fun loadDatafromDb(): LiveData<CacheObject>

    @MainThread
    protected abstract fun shouldFetch(@NotNull data: CacheObject): Boolean
}