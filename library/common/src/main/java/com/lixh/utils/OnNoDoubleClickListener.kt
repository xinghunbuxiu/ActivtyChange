package com.lixh.utils

import android.view.View

import java.util.Calendar

/**
 * des:防止重复点击
 * Created by xsf
 * on 2016.05.9:29
 */

abstract class OnNoDoubleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    protected abstract fun onNoDoubleClick(v: View)

    companion object {

        val MIN_CLICK_DELAY_TIME = 1000
    }

}