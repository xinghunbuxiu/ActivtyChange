package com.lixh.rxhttp.convert

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class JsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    /**
     * Returns a [Converter] for converting an HTTP response body to `type`, or null if
     * `type` cannot be handled by this factory. This is used to create converters for
     * response types such as `SimpleResponse` from a `Call<SimpleResponse>`
     * declaration.
     */
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val adapter = gson?.getAdapter(TypeToken.get(type))
        return JsonResponseBodyConverter(gson, adapter)
    }

    /**
     * Returns a [Converter] for converting `type` to an HTTP request body, or null if
     * `type` cannot be handled by this factory. This is used to create converters for types
     * specified by [@Body][Body], [@Part][Part], and [@PartMap][PartMap]
     * values.
     */
    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return JsonRequestBodyConverter(gson, adapter)
    }

    companion object {

        @JvmOverloads
        fun create(gson: Gson = Gson()): JsonConverterFactory {
            return JsonConverterFactory(gson)
        }
    }
}