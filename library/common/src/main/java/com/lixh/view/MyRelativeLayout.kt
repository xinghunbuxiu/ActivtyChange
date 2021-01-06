package com.lixh.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by LIXH on 2017/4/5.
 * email lixhVip9@163.com
 * des
 */

class MyRelativeLayout : RelativeLayout {
    var isCj: Boolean = true

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isCj) {
            setPadding(paddingLeft, 0, paddingRight, paddingBottom)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

    }
}
