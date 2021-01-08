package com.demon.activitychange.ui.fragment

import android.graphics.Color
import android.os.Bundle
import com.demon.activitychange.R
import com.demon.activitychange.ui.MainActivity
import com.lixh.base.BaseFragment
import com.lixh.utils.extend.utils.goPage
import kotlinx.android.synthetic.main.fragment_mine.*

class MyFragment : BaseFragment(ui = {
    titleBar {
        title = "你好"
        setTitleTextColor(Color.WHITE)
    }
    body(R.layout.fragment_mine)
}) {
    override fun init(savedInstanceState: Bundle?) {
        aaaaaaa.setOnClickListener {
            goPage(MainActivity::class.java)
        }
    }
}