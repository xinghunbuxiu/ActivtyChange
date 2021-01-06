package com.lixh.utils.extend.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.lixh.utils.ULog
import java.io.Serializable

/**
 * 直接获取intent 的传参
 */
fun <T> Fragment.getParam(name: String, default: T): T = with(arguments) {
    val res = this?.get(name) ?: default
    return res as T
}

/**
 * 直接获取intent 的传参
 */
fun <T> Activity.getParam(name: String, default: T): T = with(intent.extras) {
    val res = this?.get(name) ?: default
    return res as T
}


/**
 * 通过Class跳转界面
 */
fun ContextWrapper.startActivity(cls: Class<*>, vararg param: Pair<String, Any?>) {
    startActivity(createIntent(this,
            cls,
            param

    ))

}

/**
 * 通过Class跳转界面
 */
fun Fragment.startActivity(cls: Class<*>, vararg param: Pair<String, Any?>) {
    startActivity(createIntent(activity!!,
            cls,
            param

    ))
}

/**
 * 通过Class跳转界面 带返回
 */
fun Activity.startActivityForResult(cls: Class<*>, requestCode: Int, param: Array<out Pair<String, Any?>>) {
    startActivityForResult(createIntent(this,
            cls,
            param
    ), requestCode)
}

/**
 * 通过Class跳转界面
 */
fun Fragment.startActivityForResult(cls: Class<*>, requestCode: Int, vararg param: Pair<String, Any?>) {
    activity?.let {
        it.startActivityForResult(createIntent(it,
                cls,
                param

        ), requestCode)
    }
}

/**
 * 通过Class跳转界面
 */
fun ContextWrapper.startService(cls: Class<*>, vararg param: Pair<String, Any?>) {
    startService(createIntent(this,
            cls,
            param

    ))
}

/**
 * 通过Class跳转界面
 */
fun Fragment.startService(cls: Class<*>, vararg param: Pair<String, Any?>) {
    activity?.let {
        it.startService(createIntent(it,
                cls,
                param

        ))
    }
}

fun goSetActivity() {

}


fun createIntent(activity: Context, cls: Class<*>, param: Array<out Pair<String, Any?>>): Intent = with(Intent(activity, cls)) {
    param.forEach {
        val name = it.first
        when (val value = it.second) {
            null -> putExtra(name, value as Serializable)
            is Long -> putExtra(name, value)
            is Short -> putExtra(name, value)
            is Byte -> putExtra(name, value)
            is String -> putExtra(name, value)
            is Double -> putExtra(name, value)
            is Int -> putExtra(name, value)
            is Boolean -> putExtra(name, value)
            is Float -> putExtra(name, value)
            is Bundle -> putExtra(name, value)
            is Char -> putExtra(name, value)
            is CharSequence -> putExtra(name, value)
            is Parcelable -> putExtra(name, value)
            is FloatArray -> putExtra(name, value)
            is Serializable -> putExtra(name, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> putExtra(name, value)

                value.isArrayOf<String>() -> putExtra(name, value)

                value.isArrayOf<Parcelable>() -> putExtra(name, value)

                else -> ULog.e("Intent extra $name has wrong type ${value.javaClass.name}")

            }
            is IntArray -> putExtra(name, value)
            is LongArray -> putExtra(name, value)
            is DoubleArray -> putExtra(name, value)
            is CharArray -> putExtra(name, value)
            is ShortArray -> putExtra(name, value)
            is BooleanArray -> putExtra(name, value)
            else -> throw IllegalArgumentException("Type Error, cannot be saved!")
        }
    }
    this
}
