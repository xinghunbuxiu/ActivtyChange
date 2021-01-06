package com.lixh.rxhttp

import android.util.Log
import com.lixh.rxhttp.view.ApiProgress
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException


/**
 * des:对服务器返回数据成功和失败处理
 * Created by xsf
 * on 2016.09.9:59
 */
object UHttp {
    fun <T> execute(request: suspend () -> T?, process: ApiProgress<T>?): Job {
        val uiScope = CoroutineScope(Dispatchers.Main)  // UI主线程的CoroutineScope
        return uiScope.launch {
            try {
                process?.onStart()
                val res: T? = withContext(Dispatchers.IO) { request() }  // IO线程中执行网络请求，成功后返回这里继续执行
                res?.let {
                    process?.onSuccess(it)
                }
            } catch (e: CancellationException) {
                Log.e("executeRequest", "job cancelled")
            } catch (e: Exception) {
                Log.e("executeRequest", "request caused exception")
                process?.onError(e)
            } finally {
                process?.onComplete()
            }
        }
    }

}
