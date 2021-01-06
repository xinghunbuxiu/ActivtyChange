package com.demon.activitychange.app

import com.lixh.app.BaseApplication
import com.lixh.jsSdk.jscrawler.JsCrawler

/**
 * Created by LIXH on 2019/4/22.
 * email lixhVip9@163.com
 * des
 */
class AppContext : BaseApplication() {

    override fun onTerminate() {
        super.onTerminate()
        JsCrawler.release()
    }

    override fun init() {
        JsCrawler.initialize(this)
    }
}
