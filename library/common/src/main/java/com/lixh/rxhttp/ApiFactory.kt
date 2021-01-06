package com.lixh.rxhttp

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import retrofit2.Converter
import retrofit2.Retrofit


object ApiFactory {

    fun <T> createApi(baseUrl: String, t: Class<T>, factory: Converter.Factory, vararg interceptor: Interceptor): T =
            Retrofit.Builder()
                    .addConverterFactory(factory)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .baseUrl(baseUrl)
                    .client(ClientFactory.apply {
                    }.client().apply {
                        interceptor?.forEach {
                            addInterceptor(it)
                        }
                    }.build()).build().create(t)


}