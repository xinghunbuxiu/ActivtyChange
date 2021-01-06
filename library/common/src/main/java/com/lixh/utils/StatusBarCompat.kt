package com.lixh.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.view.ViewCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout

/**
 * 透明状态栏
 */
object StatusBarCompat {

    private val COLOR_TRANSLUCENT = Color.parseColor("#00000000")

    val DEFAULT_COLOR_ALPHA = 112

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    @TargetApi(19)
    fun setTranslucentStatus(activity: Activity, on: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val win = activity.window
            val winParams = win.attributes
            val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    /**
     * set statusBarColor
     * @param statusColor color
     * @param alpha 0 - 255
     */
    fun setStatusBarColor(activity: Activity, statusColor: Int, alpha: Int) {
        setStatusBarColor(activity, calculateStatusBarColor(statusColor, alpha))
    }

    private fun setStatusBarColor(activity: Activity, statusColor: Int) {
        val window = activity.window
        val mContentView = activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP not translucent status bar
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //Then call setStatusBarColor.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = statusColor
                //set child View not fill the system window
                val mChildView = mContentView.getChildAt(0)
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true)
                }
            } else {
                val mDecorView = window.decorView as ViewGroup
                if (mDecorView.tag != null && mDecorView.tag is Boolean && mDecorView.tag as Boolean) {
                    //if has add fake status bar view
                    val mStatusBarView = mDecorView.getChildAt(0)
                    mStatusBarView?.setBackgroundColor(statusColor)
                } else {
                    val statusBarHeight = getStatusBarHeight(activity)
                    //add margin
                    val mContentChild = mContentView.getChildAt(0)
                    if (mContentChild != null) {
                        ViewCompat.setFitsSystemWindows(mContentChild, false)
                        val lp = mContentChild.layoutParams as FrameLayout.LayoutParams
                        lp.topMargin += statusBarHeight
                        mContentChild.layoutParams = lp
                    }
                    //add fake status bar view
                    val mStatusBarView = View(activity)
                    val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
                    layoutParams.gravity = Gravity.TOP
                    mStatusBarView.layoutParams = layoutParams
                    mStatusBarView.setBackgroundColor(statusColor)
                    mDecorView.addView(mStatusBarView, 0)
                    mDecorView.tag = true
                }
            }
        }
    }

    /**
     * change to full screen mode
     * @param hideStatusBarBackground hide status bar alpha Background when SDK > 21, true if hide it
     */
    fun translucentStatusBar(activity: Activity, hideStatusBarBackground: Boolean = false) {
        val window = activity.window
        val mContentView = activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup

        //set child View not fill the system window
        var mChildView: View? = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val statusBarHeight = getStatusBarHeight(activity)

            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP just set LayoutParams.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (hideStatusBarBackground) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.statusBarColor = COLOR_TRANSLUCENT
                } else {
                    window.statusBarColor = calculateStatusBarColor(COLOR_TRANSLUCENT, DEFAULT_COLOR_ALPHA)
                }
                //must call requestApplyInsets, otherwise it will have space in screen bottom
                if (mChildView != null) {
                    ViewCompat.requestApplyInsets(mChildView)
                }
            } else {
                val mDecorView = window.decorView as ViewGroup
                if (mDecorView.tag != null && mDecorView.tag is Boolean && mDecorView.tag as Boolean) {
                    mChildView = mDecorView.getChildAt(0)
                    //remove fake status bar view.
                    mContentView.removeView(mChildView)
                    mChildView = mContentView.getChildAt(0)
                    if (mChildView != null) {
                        val lp = mChildView.layoutParams as FrameLayout.LayoutParams
                        //cancel the margin top
                        if (lp != null && lp.topMargin >= statusBarHeight) {
                            lp.topMargin -= statusBarHeight
                            mChildView.layoutParams = lp
                        }
                    }
                    mDecorView.tag = false
                }
            }
        }
    }

    //Get status bar height
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            result = context.resources.getDimensionPixelOffset(resId)
        }
        return result
    }

    //Get alpha color
    private fun calculateStatusBarColor(color: Int, alpha: Int): Int {
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }
}