package com.demon.activitychange.adapter.abslistview

interface MultiItemTypeSupport<T> {

    val viewTypeCount: Int
    fun getLayoutId(position: Int, t: T): Int

    fun getItemViewType(position: Int, t: T): Int
}