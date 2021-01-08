package com.lixh.rxhttp

import com.lixh.base.BaseResPose
import com.lixh.utils.toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


/**
 * des:对服务器返回数据成功和失败处理
 * Created by xsf
 * on 2016.09.9:59
 */
object UHttp {
    fun <T> launchData(
            block: suspend CoroutineScope.() -> BaseResPose<T>,
            ok: (T?) -> Unit,
            error: (String) -> Unit = {
                it.toast()
            },
            scope: CoroutineScope = GlobalScope
    ): Job {
        val uiScope = CoroutineScope(Dispatchers.Main)  // UI主线程的CoroutineScope
        return scope.launch {
            flow { emit(block()) }      //网络请求
                    .flowOn(Dispatchers.IO) //指定请求线程

                    .catch {
                        uiScope.launch { error(it.message ?: "") }
                    }.collect {
                        uiScope.launch {
                            if (it.ok()) {
                                ok(it.data)
                            } else {
                                error(it.msg)
                            }
                        }
                    }
        }
    }

}
