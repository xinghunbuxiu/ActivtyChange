package com.lixh.rxhttp.convert


import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.lixh.base.BaseResPose
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.io.StringReader


/**
 *
 * Created by God on 2016/8/20.
 *
 */

class JsonResponseBodyConverter<T>(private val mGson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {

        val json = verify(value.string())

        value.use {
            return adapter.read(mGson.newJsonReader(StringReader(json)))
        }

    }


    private fun verify(json: String): String {

        val isOk = json.matches("code|msg|data".toRegex())
        return when {
            !isOk -> {
                val result = BaseResPose(200, "ok", when {
                    json.startsWith("\\[|\\{") -> JSON.parse(json)
                    else -> json
                }
                )
                JSON.toJSONString(result)
            }
            else -> json
        }

    }

}