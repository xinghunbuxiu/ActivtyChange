package com.demon.activitychange.bean

import java.io.Serializable

/**
 * Created by LIXH on 2019/4/22.
 * email lixhVip9@163.com
 * des
 */
class AppInfo(
        //包名
        var packageName: String = "com.tencent.mm",
        //首页启动
        var mainName: String = "com.tencent.mm.ui.LauncherUI",
        //加载的url
        var loadUrl: String = "wechart/index.html",
        var scriptPath: String = "index.html"
) : Serializable
