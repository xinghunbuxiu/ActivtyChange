package com.lixh.base

import java.io.Serializable

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
data class BaseResPose<T>(var code: Int = 0, var msg: String, var data: T?) : Serializable {

    val message: String = msg

    val success: Boolean = 200 == code || 1 == code

    override fun toString(): String {
        return "BaseRespose{" +
                "code='" + code + '\''.toString() +
                ", msg='" + message + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }
}
