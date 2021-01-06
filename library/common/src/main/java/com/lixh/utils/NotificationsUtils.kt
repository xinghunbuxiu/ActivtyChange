package com.lixh.utils

import android.app.Notification
import android.content.Context
import android.graphics.Color

import androidx.core.app.NotificationCompat.Builder
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import java.util.ArrayList

/**
 * 判断通知栏背景颜色
 * Created by dengqu on 2016/12/12.
 */
object NotificationsUtils {
    private val TAG = NotificationsUtils::class.java.simpleName
    private val CHECK_OP_NO_THROW = "checkOpNoThrow"
    private val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
    private val COLOR_THRESHOLD = 180.0
    private var titleColor: Int = 0

    /**
     * 判断通知栏背景颜色，现在手机通知栏大部分不是白色就是黑色背景
     *
     * @param context
     * @return
     */
    fun isDarkNotifyCationBar(context: Context): Boolean {
        return !isColorSimilar(Color.BLACK, getNotificationColor(context))
    }

    private fun getNotificationColor(context: Context): Int {
        return (context as? AppCompatActivity)?.let { getNotificationColorCompat(it) }
                ?: getNotificationColorInternal(context)
    }

    private fun isColorSimilar(baseColor: Int, color: Int): Boolean {
        val simpleBaseColor = baseColor or -0x1000000
        val simpleColor = color or -0x1000000
        val baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor)
        val baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor)
        val baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor)
        val value = Math.sqrt((baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue).toDouble())
        return if (value < COLOR_THRESHOLD) {
            true
        } else false
    }

    private fun getNotificationColorInternal(context: Context): Int {
        val DUMMY_TITLE = "DUMMY_TITLE"
        val builder = Builder(context)
        builder.setContentText(DUMMY_TITLE)
        val notification = builder.build()
        val notificationRoot = notification.contentView.apply(context, FrameLayout(context)) as ViewGroup
        val titleView = notificationRoot.findViewById<View>(android.R.id.title) as TextView
        if (titleView == null) {
            iteratoryView(notificationRoot, object : Filter {
                override fun filter(view: View) {
                    if (view is TextView) {
                        if (DUMMY_TITLE == view.text.toString()) {
                            titleColor = view.currentTextColor
                        }
                    }
                }
            })
            return titleColor
        } else {
            return titleView.currentTextColor
        }

    }

    private fun getNotificationColorCompat(context: Context): Int {
        val builder = Builder(context)
        val notification = builder.build()
        val layoutId = notification.contentView.layoutId
        val notificationRoot = LayoutInflater.from(context).inflate(layoutId, null) as ViewGroup
        val titleView = notificationRoot.findViewById<View>(android.R.id.title) as TextView
        if (titleView == null) {
            val textViews = ArrayList<TextView>()
            iteratoryView(notificationRoot, object : Filter {
                override fun filter(view: View) {
                    textViews.add(view as TextView)
                }
            })

            var minTextSize = Integer.MIN_VALUE.toFloat()
            var index = 0
            var i = 0
            val j = textViews.size
            while (i < j) {
                val currentSize = textViews[i].textSize
                if (currentSize > minTextSize) {
                    minTextSize = currentSize
                    index = i
                }
                i++
            }
            return textViews[index].currentTextColor
        } else {
            return titleView.currentTextColor
        }
    }

    private fun iteratoryView(view: View?, filter: Filter?) {
        if (view == null || filter == null) {
            return
        }
        filter.filter(view)
        if (view is ViewGroup) {
            val container = view as ViewGroup?
            var i = 0
            val j = container!!.childCount
            while (i < j) {
                val child = container.getChildAt(i)
                iteratoryView(child, filter)
                i++
            }
        }
    }

    private interface Filter {
        fun filter(view: View)
    }
}