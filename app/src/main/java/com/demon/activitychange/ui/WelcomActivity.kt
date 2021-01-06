package com.demon.activitychange.ui

import com.demon.activitychange.R
import com.lixh.base.LaunchActivity


/**
 * Created by LIXH on 2017/2/buy_7.
 * email lixhVip9@163.com
 * des
 */

class WelcomeActivity : LaunchActivity(R.layout.layout_launch) {


    override fun toActivity(what: Int): Class<*> {

        return if (what == GO_HOME) TabsActivity::class.java else TabsActivity::class.java
    }

}
