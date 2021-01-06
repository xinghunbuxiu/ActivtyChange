package com.lixh.rxhttp.exception

/**
 * Created by liukun on 16/3/10.
 */
class ApiException(var code: Int, detailMessage: String) : RuntimeException(detailMessage) {

    constructor(resultCode: Int) : this(resultCode, getApiExceptionMessage(resultCode))

    companion object {


        /**
         * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
         * 需要根据错误码对错误信息进行一个转换，在显示给用户
         *
         * @param code
         * @return
         */
        private fun getApiExceptionMessage(code: Int): String {

            return when (code) {
                USER_NOT_EXIST -> "该用户不存在"
                WRONG_PASSWORD -> "密码错误"
                else -> "未知错误"
            }
        }
    }
}

const val USER_NOT_EXIST = 100

const val WRONG_PASSWORD = 101