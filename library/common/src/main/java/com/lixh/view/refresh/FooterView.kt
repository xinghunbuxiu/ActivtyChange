package com.lixh.view.refresh

import android.content.Context
import android.view.View
import com.lixh.utils.UView

abstract class FooterView(internal var context: Context,
                          val layoutId: Int = 0,
                          springView: SpringView,
                          /**
                           * 这个方法用于设置下拉最大高度(max height)，无论怎么拉动都不会超过这个高度
                           * 返回值大于0才有效，如果<=0 则默认600px
                           * 默认返回0
                           */

                          val dragMaxHeight: Int = 0) : ImplPull {
    var view: View = UView.inflate(context, layoutId, null)

    override val height: Int
        get() {
            view.measure(0, 0)
            return view.measuredHeight
        }
    override var refreshView: SpringView = springView
}