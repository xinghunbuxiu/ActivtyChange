package com.demon.activitychange.adapter.abslistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.demon.activitychange.adapter.ViewHolder

abstract class CommonAdapter<T> : BaseAdapter {
    protected var mContext: Context
    protected var mDatas: List<T>? = null
    protected var mInflater: LayoutInflater
    private var layoutId: Int = 0

    constructor(context: Context, layoutId: Int) {
        mContext = context
        mInflater = LayoutInflater.from(context)
        this.layoutId = layoutId
    }

    constructor(context: Context, layoutId: Int, datas: List<T>) {
        this.mContext = context
        mInflater = LayoutInflater.from(context)
        this.mDatas = datas
        this.layoutId = layoutId
    }

    fun setmDatas(datasatas: List<T>) {
        mDatas = datasatas
    }

    override fun getCount(): Int {
        return if (mDatas == null) 0 else mDatas!!.size
    }

    override fun getItem(position: Int): T {
        return mDatas!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position)
        convert(holder, getItem(position), position)
        return holder.convertView
    }

    abstract fun convert(holder: ViewHolder, t: T)
    fun convert(holder: ViewHolder, t: T, position: Int) {
        convert(holder, t)
    }

}
