package com.lixh.utils

import com.lixh.BuildConfig

import java.lang.reflect.ParameterizedType

/**
 * 类转换初始化
 */
object TUtil {
    fun <T> getT(o: Any, i: Int): T? {

        try {
            val clz = (o.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[i] as Class<T>
                    ?: return null
            return clz.newInstance()
        } catch (e: InstantiationException) {
            if (BuildConfig.LOG_DEBUG) {
                e.printStackTrace()
            }
        } catch (e: IllegalAccessException) {
            if (BuildConfig.LOG_DEBUG) {
                e.printStackTrace()
            }
        } catch (e: ClassCastException) {
            if (BuildConfig.LOG_DEBUG) {
                e.printStackTrace()
            }
        }

        return null
    }

    fun forName(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}
