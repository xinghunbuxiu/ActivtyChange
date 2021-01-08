package com.lixh.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lixh.BuildConfig
import com.lixh.app.BaseApplication

val versionCode
    get() = BuildConfig.VERSION_CODE

val versionName
    get() = BuildConfig.VERSION_NAME

val application
    get() = BaseApplication.appContext

//----------屏幕尺寸----------
val displayMetrics: DisplayMetrics
    get() = Resources.getSystem().displayMetrics

val screenHeight: Float
    get() = displayMetrics.heightPixels.toFloat()

val screenWidth: Float
    get() = displayMetrics.widthPixels.toFloat()


fun String.centerToast(duration: Int = Toast.LENGTH_SHORT) {
    val t = Toast.makeText(application, this, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}

fun Int.log(tag: String? = application.packageCodePath) {
    this.toString().log(tag)
}

fun String.log(tag: String? = application.packageCodePath) {
    Log.e(tag, this)
}

fun Int.centerToast(duration: Int = Toast.LENGTH_SHORT) {
    val t = Toast.makeText(application, this, duration)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
}

fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, content, duration).apply {
        show()
    }
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}


fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    application.toast(this, duration)
}

fun Int.toast(duration: Int = Toast.LENGTH_SHORT) {
    application.toast(this, duration)
}


fun <T : View> Int.layout(parent: ViewGroup? = null, block: T .() -> T) {
    return UView.inflate<T>(application, this, parent).run {
        block(this)
    }
}

infix fun ViewGroup.into(view: ViewGroup) {
    return view.addView(this)
}

val Int.stringArray
    get() = Resources.getSystem().getStringArray(this)

val Int.drawable
    get() = ContextCompat.getDrawable(application, this)

val Int.string
    get() = Resources.getSystem().getString(this)

val Int.dimen
    get() = Resources.getSystem().getDimension(this)


val Int.color
    get() = ContextCompat.getColor(application, this)


val Float.dp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
    )
val Float.px
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            this,
            Resources.getSystem().displayMetrics
    )
val Float.sp
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
    )
