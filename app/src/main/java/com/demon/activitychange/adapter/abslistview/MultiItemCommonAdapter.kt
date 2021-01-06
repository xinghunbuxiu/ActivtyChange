package com.demon.activitychange.adapter.abslistview

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.demon.activitychange.adapter.ViewHolder

abstract class MultiItemCommonAdapter<T>(context: Context, datas: List<T>,
                                         protected var mMultiItemTypeSupport: MultiItemTypeSupport<T>?) : CommonAdapter<T>(context, -1, datas) {

    init {
        if (mMultiItemTypeSupport == null)
            throw IllegalArgumentException("the mMultiItemTypeSupport can not be null.")
    }

    override fun getViewTypeCount(): Int {
        return if (mMultiItemTypeSupport != null) mMultiItemTypeSupport!!.viewTypeCount else super.getViewTypeCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mMultiItemTypeSupport != null) mMultiItemTypeSupport!!.getItemViewType(position,
                mDatas!![position]) else super.getItemViewType(position)

    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        if (mMultiItemTypeSupport == null)
            return super.getView(position, convertView, parent)

        val layoutId = mMultiItemTypeSupport!!.getLayoutId(position,
                getItem(position))
        val viewHolder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position)
        convert(viewHolder, getItem(position))
        return viewHolder.convertView
    }

}
