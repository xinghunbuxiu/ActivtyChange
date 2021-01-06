package com.lixh.view

import android.content.Context
import android.view.View
import com.lixh.utils.UView

abstract class BaseSlideView constructor(context: Context, layoutId: Int) : ISlideMenu {

    val view: View = UView.inflate<View>(context, layoutId).apply {
        initView(this)
        initListener()
    }
}