package com.lixh.utils.extend.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat

/**
 * 设置颜色直接使用colors.xml中定义的颜色即可
 */
fun TextView.setColor(resId: Int) {
    this.setTextColor(ContextCompat.getColor(this.context, resId))
}

fun TextView.setDrawableLeft(resId: Int) {
    val drawable: Drawable = ContextCompat.getDrawable(this.context, resId)!!
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(drawable, null, null, null)
}

fun TextView.setDrawableRight(resId: Int) {
    val drawable: Drawable = ContextCompat.getDrawable(this.context, resId)!!
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, null, drawable, null)
}

fun TextView.setDrawableTop(resId: Int) {
    val drawable: Drawable = ContextCompat.getDrawable(this.context, resId)!!
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, drawable, null, null)
}

fun TextView.setDrawableBottom(resId: Int) {
    val drawable: Drawable = ContextCompat.getDrawable(this.context, resId)!!
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    this.setCompoundDrawables(null, null, null, drawable)
}

//设置 描边
fun TextView.borderWidth(@Dimension rtvBorderWidth: Int, @ColorInt rtvBorderColor: Int) {
    val gd = background as GradientDrawable
    gd.setStroke(rtvBorderWidth, rtvBorderColor)
}

//背景颜色
fun TextView.backgroundColor(@ColorInt color: Int) {
    val myGrad = background as GradientDrawable
    myGrad.setColor(color)
}

//设置渐变背景
fun TextView.backgroundColor(orientation: GradientDrawable.Orientation, @ColorInt colors: IntArray) {
    background = GradientDrawable(orientation, colors)
}

//设置圆角
fun TextView.cornerRadius(r0: Float, r1: Float, r2: Float, r3: Float) {
    // 设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
    val myGrad = background as GradientDrawable
    myGrad.cornerRadii = floatArrayOf(r0, r0, r1, r1, r2, r2, r3, r3)
}

//设置圆角
fun TextView.cornerRadius(rtvRadius: Float) {
    val myGrad = background as GradientDrawable
    myGrad.cornerRadius = rtvRadius
}