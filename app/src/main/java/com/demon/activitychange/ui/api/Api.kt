package com.demon.activitychange.ui.api


import android.util.SparseArray

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lixh.rxhttp.ApiFactory
import com.lixh.rxhttp.convert.JsonConverterFactory

import okhttp3.Request

/**
 * Api Retrofit build
 */
class Api//构造方法私有
private constructor(hostType: Int) {
    var apiService: ApiService

    init {
        val paramsInterceptor = BasicParamsInterceptor.Builder().addIntercept(IRequestIntercept { this.resetRequest(it) }).build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create()
        apiService = ApiFactory.createApi(ApiConstants.getHost(hostType), ApiService::class.java, JsonConverterFactory.create(gson), paramsInterceptor)

    }

    private fun resetRequest(request: Request): Request {
        return request
    }

    companion object {
        private val sRetrofitManager = SparseArray<Api>(HostType.TYPE_COUNT)


        val default: ApiService
            get() = getDefault(1)

        /**
         * @param hostType
         */
        fun getDefault(hostType: Int): ApiService {
            var retrofitManager: Api? = sRetrofitManager.get(hostType)
            if (retrofitManager == null) {
                retrofitManager = Api(hostType)
                sRetrofitManager.put(hostType, retrofitManager)
            }
            return retrofitManager.apiService
        }
    }

}