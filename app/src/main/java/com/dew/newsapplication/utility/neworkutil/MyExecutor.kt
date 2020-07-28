package com.dew.newsapplication.utility.neworkutil

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object MyExecutor {
    private val singleExecutor = Executors.newSingleThreadExecutor()
    private val mainThreadExecutor = MainThreadExecutor()

    fun getSingleExecutor(): Executor = singleExecutor

    fun getMainThreadExecutor(): Executor = mainThreadExecutor

}

private class MainThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())
    override fun execute(p0: Runnable?) {
        mHandler.post(p0)
    }

}