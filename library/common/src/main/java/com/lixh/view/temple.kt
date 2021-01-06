package com.lixh.view

import android.app.Activity
import android.view.View
import com.lixh.utils.UView


inline fun IBase.ui(block: ExtandView.() -> Unit) = ExtandView(this).run {
    block(this)
    this
}

//正文
inline fun <T : View> inflate(activity: Activity, resId: Int, block: T.() -> Unit) = UView.inflate<T>(activity, resId).run { block(this) }


fun gone(vararg views: View) {
    views.forEach { it.visibility = View.GONE }

}

fun visible(vararg views: View) {
    views.forEach { it.visibility = View.VISIBLE }

}