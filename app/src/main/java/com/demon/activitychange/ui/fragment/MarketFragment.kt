package com.demon.activitychange.ui.fragment

import android.graphics.Color
import android.os.Bundle
import com.demon.activitychange.R
import com.lixh.base.BaseFragment

class MarketFragment : BaseFragment(ui = {
    titleBar {
        title = "你好"
        setTitleTextColor(Color.WHITE)
    }
    body(R.layout.fragment_mine)
}) {

    override fun init(savedInstanceState: Bundle?) {
    }
}