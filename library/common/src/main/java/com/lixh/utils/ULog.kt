package com.lixh.utils


import com.lixh.logger.LogLevel
import com.lixh.logger.Logger
import com.lixh.logger.LoggerPrinter
import com.lixh.logger.LoggerPrinter.Companion.LOCAL_METHOD_COUNT
import com.lixh.logger.LoggerPrinter.Companion.LOCAL_TAG
import com.lixh.logger.Settings
import com.lixh.logger.Settings.logLevel
import com.lixh.setting.AppConfig
import com.lixh.utils.UProperties.TAG

/**
 * 如果用于android平台，将信息记录到“LogCat”
 */
object ULog {
    var DEBUG_ENABLE = false// 是否调试模式
    /**
     * @return the appropriate tag based on local or global
     */
    private val tag: String
        get() {
            val tag = LoggerPrinter.LOCAL_TAG.get()
            if (tag != null) {
                LOCAL_TAG.remove()
                return tag
            }
            return TAG
        }

    private val methodCount: Int
        get() {
            val count = LOCAL_METHOD_COUNT.get()
            var result = Settings.methodCount
            if (count != null) {
                LOCAL_METHOD_COUNT.remove()
                result = count
            }
            if (result < 0) {
                throw IllegalStateException("methodCount cannot be negative")
            }
            return result
        }

    /**
     * 在application调用初始化
     */
    fun logInit(debug: Boolean) {
        DEBUG_ENABLE = debug
        // default LogLevel.FULL
        if (DEBUG_ENABLE) {
            Logger.init(AppConfig.DEBUG_TAG)                 // default PRETTYLOGGER or use just init()
            Settings.methodCount = 2
            logLevel = LogLevel.FULL
        }
    }

    fun d(tag: String, message: String) {
        if (DEBUG_ENABLE) {
            Logger.d(tag, message)
        }
    }

    fun d(message: String) {
        if (DEBUG_ENABLE) {
            Logger.d(message)
        }
    }

    fun w(message: String) {
        if (DEBUG_ENABLE) {
            Logger.w(message)
        }
    }

    fun e(e: String) {
        if (DEBUG_ENABLE) {
            Logger.e(e)
        }
    }

    fun e(throwable: Throwable, message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.e(throwable, message, *args)
        }
    }

    fun e(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.e(message, *args)
        }
    }

    fun i(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.i(message, *args)
        }
    }

    fun v(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.v(message, *args)
        }
    }

    fun w(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.v(message, *args)
        }
    }

    fun wtf(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.wtf(message, *args)
        }
    }

    fun json(message: String) {
        if (DEBUG_ENABLE) {
            Logger.json(message)
        }
    }

    fun xml(message: String) {
        if (DEBUG_ENABLE) {
            Logger.xml(message)
        }
    }
}
