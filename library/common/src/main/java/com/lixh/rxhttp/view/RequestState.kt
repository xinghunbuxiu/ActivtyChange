package com.lixh.rxhttp.view;

open interface RequestState<T> {

    fun onStart()

    fun onSuccess(t: T)

    fun onError(e: Throwable)

    fun onComplete()
}
