package com.lixh.utils

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.flyco.dialog.listener.OnBtnClickL
import com.lixh.R

/**
 * @author Administrator 更新应用版本API
 */
class UpdateHelper private constructor(context: Context, internal var builder: Builder) {
    private var broadcastReceiver: BroadcastReceiver? = null
    private var isRegisterBR: Boolean = false
    private var versionName: String? = null
    internal var resources: Resources
    private var isFirst by UStore("firstLogin", true)
    private var lastUpdateTime by UStore("lastUpdateTime", 0L)

    companion object {
        lateinit var mContext: Context
    }

    init {
        mContext = context
        resources = context.resources
    }

    /**
     * 下载完成之后更新UI
     */
    internal var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Alert.dismiss()
            val msgStr = msg.obj.toString()
            when (msg.what) {
                1 -> builder.onResultListener?.parse(msgStr)
                2 -> R.string.not_connect_networks.toast()
            }
        }
    }

    var showUpdate: Runnable = Runnable {
        // 更新
        Alert.displayAlertDialog(context, "发现新版本", "最新版本:" + "\n"
                + versionName, "立即更新", "以后再说", OnBtnClickL {
            if (UFile.isExistsSdcard) {// 判断sdcard是否存在
                val updateIntent = Intent(context,
                        UpdateService::class.java)
                context.startService(updateIntent)
                context.bindService(updateIntent, builder.serviceConnection,
                        Context.BIND_AUTO_CREATE)

                R.string.backstage_down.toast(Toast.LENGTH_LONG)
            } else {
                R.string.plug_sdcard_tip.toast(Toast.LENGTH_LONG)
            }
        }, null)
    }


    fun check() {
        if (UNetWork.isNetworkAvailable(mContext)) {
            if (null != broadcastReceiver && isRegisterBR) {
                mContext.unregisterReceiver(broadcastReceiver)
                isRegisterBR = false
            }
            checkUpdate()
        } else {
            if (!isRegisterBR) {
                broadcastReceiver = object : BroadcastReceiver() {
                    override fun onReceive(mContext: Context, intent: Intent) {
                        checkUpdate()
                    }
                }
                val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                mContext.registerReceiver(broadcastReceiver, intentFilter)
                isRegisterBR = true
            }
        }
    }

    // 检查更新
    fun checkUpdate() {
        val cTime = System.currentTimeMillis()
        if (!isFirst) {
            execCheckUpdate()
        } else {
            lastUpdateTime = cTime
            isFirst = false
        }
    }

    private fun execCheckUpdate() {
        /* 检查上次更新时间到现在是否满足一天 */
        if (lastUpdateTime + builder.delay < System
                        .currentTimeMillis()) {
            /* 保存当前时间给下次更新使用 */
            lastUpdateTime = System.currentTimeMillis()
            /* 开始检查更新 */
            checkUpdateInfo()
        }
    }

    // 外部接口让主Activity调用
    private fun checkUpdateInfo() {// 此线程负责在后台检查是否有新版本
        object : Thread() {
            override fun run() {
                val msg = mHandler.obtainMessage()
                try {
                    if (UNetWork.isNetworkAvailable(mContext)) {// 判断网络是否可用
                        //请求
                        msg.obj = builder.onResultListener!!.post()
                        msg.what = 1
                    } else {
                        // 网络异常时
                        msg.obj = resources.getString(R.string.not_connect_networks)
                        msg.what = 2
                    }
                } catch (e: Exception) {
                    msg.obj = resources.getString(R.string.not_connect_networks)
                    msg.what = 2
                }

                msg.sendToTarget()
            }
        }.start()
    }

    class Builder(private val context: Context) {

        //更新时间
        var delay: Long = 0

        var serviceConnection: ServiceConnection? = null

        var onResultListener: UpdateFactory.OnResultListener? = null

        companion object {
            fun with(context: Context, body: Builder.() -> UpdateHelper): UpdateHelper {

                return with(Builder(context)) {
                    body()
                }

            }

        }


        fun build(): UpdateHelper {
            return UpdateHelper(context, this)
        }

    }


}
