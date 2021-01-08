@file:Suppress("UNCHECKED_CAST")

package com.lixh.utils


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 类名: [UView]
 * <br></br> 功能描述: 存储全局数据的基本类
 * <br></br> 作者: MouTao
 * <br></br> 时间: 2017/5/19
 */
object UView {

    /**
     * view 实例
     *
     * @param context
     * @param res
     * @return
     */
    fun <T : View> inflate(context: Context, res: Int): T {
        return inflate(context, res, null)

    }

    /**
     * view 实例
     *
     * @param context
     * @param parent
     * @param res
     * @return
     */
    fun <T : View> inflate(context: Context, res: Int, parent: ViewGroup?, attachToRoot: Boolean = false): T {
        return with(LayoutInflater.from(context)) {
            inflate(res, parent, attachToRoot) as T
        }
    }

}
