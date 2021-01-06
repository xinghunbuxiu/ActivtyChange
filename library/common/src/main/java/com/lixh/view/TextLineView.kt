package com.lixh.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

import com.lixh.R


/**
 * TODO: document your custom view class.
 */
class TextLineView : AppCompatTextView {
    var lineHeight = 4f
    internal var mTextWidth: Float = 0.toFloat()
    internal var mTextHeight: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.TextLineView, defStyle, 0)
        lineHeight = a.getDimension(
                R.styleable.TextLineView_lineHeight,
                lineHeight)
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        mTextWidth = paint.measureText(text.toString())

        val fontMetrics = paint.fontMetrics
        mTextHeight = fontMetrics.bottom
        canvas.drawLine(0f, (contentHeight - lineHeight) / 2, (contentWidth - mTextWidth) / 2, (contentHeight + lineHeight) / 2, paint)
        canvas.drawLine((contentWidth + mTextWidth) / 2, (contentHeight - lineHeight) / 2, contentWidth.toFloat(), (contentHeight + lineHeight) / 2, paint)

    }


}
