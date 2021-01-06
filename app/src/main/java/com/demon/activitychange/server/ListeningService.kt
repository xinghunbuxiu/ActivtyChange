package com.demon.activitychange.server

import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.alibaba.fastjson.JSON
import com.demon.activitychange.uiTools.GlobalView
import com.demon.activitychange.bean.AccessibilityEventBean
import com.demon.activitychange.bean.AppInfo
import com.lixh.jsSdk.AccessibilityUtil
import com.lixh.jsSdk.base.BaseAccessibilityService
import com.lixh.jsSdk.jscrawler.JsCrawler
import com.lixh.utils.UFile
import java.util.*

/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
class ListeningService : BaseAccessibilityService() {
    private var appInfoMap: MutableMap<CharSequence, AppInfo> = HashMap()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        val appInfo = intent.getSerializableExtra("appinfo") as AppInfo
        appInfoMap[appInfo.packageName] = appInfo
        val path = "file:///" + UFile.cacheDir + "/" + appInfo.loadUrl
        JsCrawler.getInstance().loadUrl(path)
        AccessibilityUtil.JumpToOtherApp(this, appInfo.packageName, appInfo.mainName)
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onTypeWindowStateChanged(event: AccessibilityEvent) {
        super.onTypeWindowStateChanged(event)
        GlobalView.init(this).showView(event.packageName.toString() + "\n" + event.className)
        val bean = AccessibilityEventBean(event.packageName, event.className, event.text)
        Log.e(TAG, JSON.toJSONString(bean))
        JsCrawler.getInstance().loadWebViewInterface("onPageChanged", JSON.toJSON(bean))
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalView.init(this).removeView()
    }

    companion object {
        private const val TAG = "WindowChange"
    }

}

