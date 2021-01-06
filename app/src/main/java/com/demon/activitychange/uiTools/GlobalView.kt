package com.demon.activitychange.uiTools

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.demon.activitychange.R
import com.yhao.floatwindow.FloatWindow
import com.yhao.floatwindow.MoveType
import com.yhao.floatwindow.Screen
import com.yhao.floatwindow.Util
import kotlinx.android.synthetic.main.item_history.view.*

/**
 * Created by LIXH on 2019/4/8.
 * email lixhVip9@163.com
 * des
 */
class GlobalView(mContext: Context) {
    private var mView: View = Util.inflate(mContext, R.layout.item_history)

    init {
        FloatWindow.with(mContext)
                .setView(mView)
                .setWidth(Screen.width, 0.6f)
                .setHeight(Screen.width, 0.4f)
                .setMoveType(MoveType.active)
                .setDesktopShow(true)
                .build()
    }

    fun showView(s: String) {
        mView.historyWindowName.text = s
        FloatWindow.get().show()
    }

    fun removeView() {
        FloatWindow.get().hide()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: GlobalView? = null

        fun init(context: Context) =
                instance ?: synchronized(this) {
                    instance ?: GlobalView(context).also { instance = it }
                }

    }
}
