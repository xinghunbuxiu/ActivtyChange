package com.lixh.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder

/**
 * @author Administrator 更新应用版本API
 */
@SuppressLint("StaticFieldLeak")
object UpdateFactory {
    private var mNotificationManager: NotificationManager? = null
    @Volatile
    private var updateHelper: UpdateHelper? = null
    private var binder: UpdateService.DownloadBinder? = null
    internal var isBind = false
    lateinit var context: Context
    internal var apkUrl = UFile.cacheDir
    internal var downApkUrl: String? = null

    var resultListener: OnResultListener = object : OnResultListener {

        override fun post(): String {
            return ""
        }

        override fun parse(msg: String) {
            //解析
        }
    }


    private val callback = object : ICallbackResult {
        override fun onBackResult(result: Any) {
            if ("finish" == result) {
                return
            }
        }

    }
    private var conn: ServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            isBind = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            binder = service as UpdateService.DownloadBinder
            println("服务启动!!!")
            // 开始下载
            isBind = true
            binder?.addCallback(callback)
            downApkUrl?.let { binder?.start(it, apkUrl) }

        }
    }

    interface ICallbackResult {
        fun onBackResult(result: Any)
    }

    interface OnResultListener {
        fun post(): String

        fun parse(msg: String)
    }

    fun init(context: Activity) {
        this.context = context
        apkUrl = Global.get()?.packageName.plus(".apk")
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun build(): UpdateHelper? {
        updateHelper = UpdateHelper.Builder.with(context) {
            delay = (1000 * 60 * 60 * 24).toLong()
            serviceConnection = conn
            onResultListener = resultListener
            build()
        }
        return updateHelper
    }

}
