package com.demon.activitychange.ui

import android.annotation.SuppressLint
import android.os.Bundle
import com.demon.activitychange.R
import com.demon.activitychange.bean.AppInfo
import com.demon.activitychange.server.ListeningService
import com.lixh.base.BaseActivity
import com.lixh.jsSdk.AccessibilityUtil
import com.lixh.utils.extend.utils.startService
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("SetTextI18n")
class MainActivity : BaseActivity(
        ui = {
            titleBar {
                title = "nihao"
            }
            body(R.layout.activity_main)
            swipeBack {}
        }
) {
    private val isServiceStart: Boolean
        get() = AccessibilityUtil.isAccessibilitySettingsOn(this, ListeningService::class.java.canonicalName)

    override fun init(savedInstanceState: Bundle?) {
        val appInfo = AppInfo()

//        if (isServiceStart) {
//            GlobalView.init(this).showView(packageName + "\n" + javaClass.canonicalName)
//            tv_status.text = "服务已开启"
//        } else {
//            tv_status.text = "服务未开启"
//        }
//        bt_select_file.setOnClickListener {
//            val file = "wechart.zip"
//            askPermissions(*PermissionUtils.PERMISSIONS) {
//                onGranted {
//                    val saveFile = UFile.cacheDir
//                    filePath.setText(saveFile)
//                    try {
//                        val inputStream = assets.open(file)
//                        ZipUtils.Unzip(inputStream, saveFile)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//                onDenied { permissions ->
//                    permissions.forEach {
//                        Log.d(LaunchActivity.TAG, "Call $it is denied")
//                    }
//                }
//
//            }
//
//        }
        begin.setOnClickListener {
            if (!isServiceStart) {
                AccessibilityUtil.goAccess(this)
            } else {
                tv_status.text = "服务已开启"
                startService(ListeningService::class.java, "appinfo" to appInfo)
            }
        }
    }

}