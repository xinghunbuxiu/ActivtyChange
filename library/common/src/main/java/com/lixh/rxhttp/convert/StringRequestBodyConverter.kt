package com.lixh.rxhttp.convert

import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter

/**
 * Created by 耿 on 2016/9/6.
 */
class StringRequestBodyConverter internal constructor() : Converter<String, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: String): RequestBody? {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        writer.write(value)
        writer.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
        private val UTF_8 = Charset.forName("UTF-8")
    }
}