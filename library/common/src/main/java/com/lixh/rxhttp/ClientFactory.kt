package com.lixh.rxhttp


import com.lixh.BuildConfig.LOG_DEBUG
import com.lixh.app.BaseApplication
import okhttp3.Cache
import okhttp3.OkHttpClient.Builder
import java.io.File
import java.util.concurrent.TimeUnit

object ClientFactory {
    private const val TIMEOUT_CONNECTION: Long = 15L
    private const val TIMEOUT_READ: Long = 15L
    var socketFactory: Boolean = false
    var certficates: IntArray? = null
    var hosts: Array<String>? = null

    fun client(): Builder = Builder().apply {
        if (LOG_DEBUG) {
            addInterceptor(ClientHelper.httpLoggingInterceptor)
        }
        if (socketFactory) {
            certficates?.let { ClientHelper.getSSLSocketFactory(BaseApplication.appContext, it) }?.let { socketFactory(it) }
            hosts?.let { ClientHelper.getHostnameVerifier(it) }?.let { hostnameVerifier(it) }
        }
        val cacheFile = File(BaseApplication.appContext.cacheDir, "cache")
        val cache = Cache(cacheFile, (10 * 1024 * 1024).toLong())
        addNetworkInterceptor(ClientHelper.autoCacheInterceptor())
        addInterceptor(ClientHelper.autoCacheInterceptor())
        cache(cache)
        retryOnConnectionFailure(true)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build()
    }

}