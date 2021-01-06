package com.lixh.utils

import android.app.Dialog
import android.app.Service
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lixh.BuildConfig
import com.lixh.app.BaseApplication

val versionCode
    get() = BuildConfig.VERSION_CODE

val application
    get() = BaseApplication.appContext

//----------屏幕尺寸----------
val Context.displayMetrics: DisplayMetrics
    get() {
        val diametric = DisplayMetrics()
        val windowManager = this.getSystemService(
                Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(diametric)
        return diametric
    }

val Context.screenHeight: Float
    get() = displayMetrics.heightPixels.toFloat()

val Context.screenWidth: Float
    get() = displayMetrics.widthPixels.toFloat()


//----------toast----------
fun showShort(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(application, text, duration).show()
}

//----------toast----------
fun showShort(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(application, resId, duration).show()
}

//----------toast----------
fun showLong(text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(application, text, duration).show()
}

//----------toast----------
fun showLong(resId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(application, resId, duration).show()
}


fun centerToast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    var t = Toast.makeText(application, resId, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}


fun Context.color(id: Int) = ContextCompat.getColor(this, id)

fun Context.string(id: Int): String = resources.getString(id)

fun Context.stringArray(id: Int): Array<String> = resources.getStringArray(id)

fun Context.drawable(id: Int) = ContextCompat.getDrawable(this, id)

fun Context.dimen(id: Int) = resources.getDimension(id)

fun Context.dp2px(dp: Float): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

fun Context.px2dp(px: Float): Int = (px / resources.displayMetrics.density + 0.5f).toInt()

fun Context.sp2px(sp: Float): Int = (sp * resources.displayMetrics.scaledDensity + 0.5f).toInt()

fun Context.px2sp(px: Float): Int = (px / resources.displayMetrics.scaledDensity + 0.5f).toInt()


/**
 * 从Fragment中获取资源
 */
fun Fragment.color(id: Int) = context!!.color(id)

fun Fragment.string(id: Int) = context!!.string(id)

fun Fragment.stringArray(id: Int) = context!!.stringArray(id)

fun Fragment.drawable(id: Int) = context!!.drawable(id)

fun Fragment.dimen(id: Int) = context!!.dimen(id)

fun Fragment.dp2px(dp: Float): Int = context!!.dp2px(dp)

fun Fragment.px2dp(px: Float): Int = context!!.px2dp(px)

fun Fragment.sp2px(sp: Float): Int = context!!.sp2px(sp)

fun Fragment.px2sp(px: Float): Int = context!!.px2sp(px)


/**
 * 从Dialog中获取资源
 */
fun Dialog.color(id: Int) = context.color(id)

fun Dialog.string(id: Int) = context.string(id)

fun Dialog.stringArray(id: Int) = context.stringArray(id)

fun Dialog.drawable(id: Int) = context.drawable(id)

fun Dialog.dimen(id: Int) = context.dimen(id)

fun Dialog.dp2px(dp: Float): Int = context.dp2px(dp)

fun Dialog.px2dp(px: Float): Int = context.px2dp(px)

fun Dialog.sp2px(sp: Float): Int = context.sp2px(sp)

fun Dialog.px2sp(px: Float): Int = context.px2sp(px)

/**
 * 从service中获取资源
 */
fun Service.color(id: Int) = applicationContext.color(id)

fun Service.string(id: Int) = applicationContext.string(id)

fun Service.stringArray(id: Int) = applicationContext.stringArray(id)

fun Service.drawable(id: Int) = applicationContext.drawable(id)

fun Service.dimen(id: Int) = applicationContext.dimen(id)

fun Service.dp2px(dp: Float): Int = applicationContext.dp2px(dp)

fun Service.px2dp(px: Float): Int = applicationContext.px2dp(px)

fun Service.sp2px(sp: Float): Int = applicationContext.sp2px(sp)

fun Service.px2sp(px: Float): Int = applicationContext.px2sp(px)

/**
 * 从View中获取资源
 */
fun View.color(id: Int) = context.color(id)

fun View.string(id: Int) = context.string(id)

fun View.stringArray(id: Int) = context.stringArray(id)

fun View.drawable(id: Int) = context.drawable(id)

fun View.dimen(id: Int) = context.dimen(id)

fun View.dp2px(dp: Float): Int = context.dp2px(dp)

fun View.px2dp(px: Float): Int = context.px2dp(px)

fun View.sp2px(sp: Float): Int = context.sp2px(sp)

fun View.px2sp(px: Float): Int = context.px2sp(px)

/**
 * 从RecyclerView中获取资源
 */
fun RecyclerView.ViewHolder.color(id: Int) = itemView.color(id)

fun RecyclerView.ViewHolder.string(id: Int) = itemView.string(id)

fun RecyclerView.ViewHolder.stringArray(id: Int) = itemView.stringArray(id)

fun RecyclerView.ViewHolder.drawable(id: Int) = itemView.drawable(id)

fun RecyclerView.ViewHolder.dimenPx(id: Int) = itemView.dimen(id)

fun RecyclerView.ViewHolder.dp2px(dp: Float): Int = itemView.dp2px(dp)

fun RecyclerView.ViewHolder.px2dp(px: Float): Int = itemView.px2dp(px)

fun RecyclerView.ViewHolder.sp2px(sp: Float): Int = itemView.sp2px(sp)

fun RecyclerView.ViewHolder.px2sp(px: Float): Int = itemView.px2sp(px)