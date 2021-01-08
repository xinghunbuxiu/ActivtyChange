package com.xbtx.shopManager.api


import android.util.SparseArray
import com.demon.activitychange.ui.api.HostType
import com.google.gson.GsonBuilder
import com.lixh.rxhttp.ApiFactory
import com.lixh.rxhttp.convert.JsonConverterFactory
import com.xbtx.shopManager.api.BasicParamsInterceptor.IRequestIntercept
import okhttp3.Request

/**
 * Api Retrofit build
 */
class Api(hostType: Int) {

    var apiService: ApiService

    init {
        val paramsInterceptor = BasicParamsInterceptor.Builder()
                .addIntercept(object : IRequestIntercept {
                    override fun intercept(request: Request): Request {
                        return resetRequest(request)
                    }
                })
                .build()
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create()
        apiService = ApiFactory.createApi(
                ApiConstants.getHost(hostType),
                ApiService::class.java,
                JsonConverterFactory.create(gson),
                paramsInterceptor
        )

    }

    private fun resetRequest(request: Request): Request {
        return request
    }

    companion object {
        private val sRetrofitManager = SparseArray<Api>(HostType.TYPE_COUNT)

        @JvmStatic
        val default: ApiService
            get() = getDefault(1)

        /**
         * @param hostType
         */
        @JvmStatic
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