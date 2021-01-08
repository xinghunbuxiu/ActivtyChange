package com.lixh.presenter

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.lixh.rxhttp.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


/**
 * des:基类presenter
 * Created by xsf
 * on 2016.07.11:55
 */
@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : LifecycleOwner> : CoroutineScope by MainScope() {
    var activity: Activity? = null
    var v: V? = null

    fun onDestroy() {
        cancel()
    }

    fun <T> get() = v as T

    fun attach(view: LifecycleOwner) {
        v = view as V
        activity = when (view) {
            is FragmentActivity -> {
                view
            }
            is Fragment -> {
                view.activity
            }
            else -> null
        }
    }


}
