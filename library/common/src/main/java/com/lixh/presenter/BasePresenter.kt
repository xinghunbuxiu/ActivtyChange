package com.lixh.presenter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lixh.utils.UIntent
import com.lixh.view.IBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


/**
 * des:基类presenter
 * Created by xsf
 * on 2016.07.11:55
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : IBase> : CoroutineScope by MainScope() {
    var intent: UIntent? = null
    var activity: Activity? = null
    var v: V? = null

    fun onDestroy() {
        cancel()
    }

    fun <T> get() = v as T

    fun attach(view: IBase) {
        v = view as V
        activity = when (view) {
            is FragmentActivity -> {
                intent = UIntent(view)
                view
            }
            is Fragment -> {
                intent = UIntent(view.activity!!)
                view.activity
            }
            else -> null
        }

    }


}
