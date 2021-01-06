package com.lixh.rxhttp.exception

interface ApiErrorCode {
    companion object {

        /**
         * 客户端错误
         */
        const val ERROR_CLIENT_AUTHORIZED = "1"
        /**
         * 用户授权失败
         */
        const val ERROR_USER_AUTHORIZED = "2"
        /**
         * 请求参数错误
         */
        const val ERROR_REQUEST_PARAM = "3"
        /**
         * 参数检验不通过
         */
        const val ERROR_PARAM_CHECK = "4"
        /**
         * 自定义错误
         */
        const val ERROR_OTHER = "10"
        /**
         * 无网络连接
         */
        const val ERROR_NO_INTERNET = "11"
    }

}