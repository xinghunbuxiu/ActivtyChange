package com.lixh.utils

import android.view.View

/**
 * des:双击事件
 * Created by xsf
 * on 2016.07.10:58
 */
abstract class OnDoubleClickListener : View.OnClickListener {
    private var count = 0
    private var firClick: Long = 0
    private var secClick: Long = 0

    override fun onClick(v: View) {
        count++
        if (count == 1) {
            firClick = System.currentTimeMillis()

        } else if (count == 2) {
            secClick = System.currentTimeMillis()
            if (secClick - firClick < 1000) {
                //双击事件
                onDoubleClick(v)
            }
            count = 0
            firClick = 0
            secClick = 0
        }
    }

    protected abstract fun onDoubleClick(v: View)
}