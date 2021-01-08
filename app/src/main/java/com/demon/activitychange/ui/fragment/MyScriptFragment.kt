package com.demon.activitychange.ui.fragment

import android.os.Bundle
import com.demon.activitychange.R
import com.lixh.base.BaseFragment

class MyScriptFragment : BaseFragment(ui = {
    titleBar {
        title = "小爱"
    }
    body(R.layout.fragment_mine)
}) {
    override fun init(savedInstanceState: Bundle?) {

    }

}
