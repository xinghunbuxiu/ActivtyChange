package com.xbtx.shopManager.api

import android.text.TextUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException
import java.util.*
import kotlin.collections.Map.Entry


/**
 * Created by jk.yeo on 16/3/4 15:28.
 * Mail to ykooze@gmail.com
 */
class BasicParamsInterceptor private constructor() : Interceptor {
    internal var queryParamsMap: MutableMap<String, String> = HashMap()
    internal var paramsMap: MutableMap<String, String> = HashMap()
    internal var headerParamsMap: MutableMap<String, String> = HashMap()
    internal var headerLinesList: MutableList<String> = ArrayList()
    private var userAgent = "Android"
    internal var requestIntercept: IRequestIntercept? = null

    interface IRequestIntercept {
        fun intercept(request: Request): Request
    }

    fun setRequestIntercept(requestIntercept: IRequestIntercept) {
        this.requestIntercept = requestIntercept
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request= chain.request()
        requestIntercept?.let {
            request = it.intercept(request)
        }
        val requestBuilder = request.newBuilder()
        val headerBuilder = request.headers.newBuilder()
        if (this.headerParamsMap.isNotEmpty()) {
            val iterator = this.headerParamsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Entry<*, *>
                headerBuilder.add(entry.key as String, entry.value as String)
            }
        }
        if (this.headerLinesList.size > 0) {
            for (line in this.headerLinesList) {
                headerBuilder.add(line)
            }
        }
        if (this.queryParamsMap.isNotEmpty()) {
            if (request.method == "GET") {
                request = this.injectParamsIntoUrl(
                    request.url.newBuilder(),
                    requestBuilder,
                    this.queryParamsMap
                )!!
            }
        }
        // process post body inject
        if (this.paramsMap.isNotEmpty()) {
            if (this.canInjectIntoBody(request)) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in this.paramsMap) {
                    formBodyBuilder.add(key, value)
                }

                val formBody = formBodyBuilder.build()
                var postBodyString = bodyToString(request.body)
                postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(
                    formBody
                )
                requestBuilder.post(
                    postBodyString
                        .toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
                )
            }
        }
        requestBuilder.headers(headerBuilder.build())
        request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method, "POST")) {
            return false
        }
        val body = request.body ?: return false
        val mediaType = body.contentType() ?: return false
        return TextUtils.equals(mediaType.subtype, "x-www-form-urlencoded")
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(
        httpUrlBuilder: HttpUrl.Builder,
        requestBuilder: Request.Builder,
        paramsMap: Map<String, String>
    ): Request? {
        if (paramsMap.isNotEmpty()) {
            val iterator = paramsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Entry<*, *>
                httpUrlBuilder.addQueryParameter(entry.key as String, entry.value as String)
            }
            requestBuilder.url(httpUrlBuilder.build())
            return requestBuilder.build()
        }

        return null
    }

    fun setUserAgent(userAgent: String) {
        this.userAgent = userAgent
    }

    class Builder {
        internal var interceptor: BasicParamsInterceptor

        init {
            this.interceptor = BasicParamsInterceptor()
        }

        fun setRequestIntercept(intercept: IRequestIntercept) {
            this.interceptor.setRequestIntercept(intercept)
        }

        fun setUserAgent(userAgent: String) {
            interceptor.setUserAgent(userAgent)
        }

        fun addParam(key: String, value: String): Builder {
            this.interceptor.paramsMap[key] = value
            return this
        }

        fun addParamsMap(paramsMap: Map<String, String>): Builder {
            this.interceptor.paramsMap.putAll(paramsMap)
            return this
        }

        fun addHeaderParam(key: String, value: String): Builder {
            this.interceptor.headerParamsMap[key] = value
            return this
        }

        fun addHeaderParamsMap(headerParamsMap: Map<String, String>): Builder {
            this.interceptor.headerParamsMap.putAll(headerParamsMap)
            return this
        }

        fun addHeaderLine(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            if (index == -1) {
                throw IllegalArgumentException("Unexpected header: $headerLine")
            }
            this.interceptor.headerLinesList.add(headerLine)
            return this
        }

        fun addHeaderLinesList(headerLinesList: List<String>): Builder {
            for (headerLine in headerLinesList) {
                val index = headerLine.indexOf(":")
                if (index == -1) {
                    throw IllegalArgumentException("Unexpected header: $headerLine")
                }
                this.interceptor.headerLinesList.add(headerLine)
            }
            return this
        }

        fun addQueryParam(key: String, value: String): Builder {
            this.interceptor.queryParamsMap[key] = value
            return this
        }

        fun addQueryParamsMap(queryParamsMap: Map<String, String>): Builder {
            this.interceptor.queryParamsMap.putAll(queryParamsMap)
            return this
        }

        fun build(): BasicParamsInterceptor {
            return this.interceptor
        }

        fun addIntercept(intercept: IRequestIntercept): Builder {
            interceptor.setRequestIntercept(intercept)
            return this
        }
    }

    companion object {

        internal fun pathSegmentsToString(pathSegments: List<String>): String {
            val out = StringBuilder()
            var i = 0
            val size = pathSegments.size
            while (i < size) {
                out.append('/')
                out.append(pathSegments[i])
                i++
            }
            return out.toString()
        }

        private fun bodyToString(request: RequestBody?): String {
            try {
                val buffer = Buffer()
                if (request != null)
                    request.writeTo(buffer)
                else
                    return ""
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "did not work"
            }

        }
    }
}