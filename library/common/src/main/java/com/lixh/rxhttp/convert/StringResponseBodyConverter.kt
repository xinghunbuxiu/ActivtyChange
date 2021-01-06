package com.lixh.rxhttp.convert

import java.io.IOException

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Created by 耿 on 2016/9/6.
 */
class StringResponseBodyConverter : Converter<ResponseBody, String> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): String? {
        value.use {
            return it.string()
        }
    }
}