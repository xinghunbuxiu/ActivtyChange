package com.lixh.base

import android.view.View
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.coroutines.runBlocking

fun <T> EasyAdapter(@LayoutRes layoutResId: Int,
                    data: MutableList<T>? = null, block: (view: View, position: Int, T) -> Unit): BaseQuickAdapter<T, BaseViewHolder> = object : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data) {


    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(holder: BaseViewHolder, item: T) = runBlocking { block(holder.itemView, holder.position, item) }

}

fun <T : MultiItemEntity> EasyMultiItemAdapter(param: Map<Int, Pair<Int, (view: View, position: Int, T) -> Unit>>,
                                               data: MutableList<T>? = null): BaseQuickAdapter<T, BaseViewHolder> = object : BaseMultiItemQuickAdapter<T, BaseViewHolder>(data) {

    init {
        for ((key, value) in param)
            addItemType(key, value.first)
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(holder: BaseViewHolder, item: T) = runBlocking {
        if (param.containsKey(item.itemType)) {
            param[item.itemType]?.second(holder.itemView, holder.position, item)
        }
    }

}

