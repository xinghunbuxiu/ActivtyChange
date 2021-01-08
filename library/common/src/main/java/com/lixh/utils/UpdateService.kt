package com.lixh.utils

import android.app.DownloadManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.content.FileProvider
import com.lixh.BuildConfig
import com.lixh.base.LaunchActivity
import java.io.File


class UpdateService : Service() {
    lateinit var dm: DownloadManager
    var enqueue: Long = 0
    private var callback: UpdateFactory.ICallbackResult? = null
    private var binder: DownloadBinder? = null
    private val mContext = this
    lateinit var receiver: BroadcastReceiver

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    override fun onCreate() {
        super.onCreate()
        binder = DownloadBinder()
        stopForeground(true)// 这个不确定是否有作用
    }

    inner class DownloadBinder : Binder() {
        fun start(downloadUrl: String, saveFileName: String) {
            try {
                receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        install(saveFileName)
                        //销毁当前的Service
                        stopSelf()
                    }
                }
                registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//                //下载需要写SD卡权限, targetSdkVersion>=23 需要动态申请权限
//                askPermissions(*PermissionUtils.EXTERNAL_GROUP) {
//                    onGranted {
//                        startDownload(downloadUrl, saveFileName)
//                    }
//                    onDenied { permissions ->
//                        permissions.forEach {
//                            Log.d(LaunchActivity.TAG, "Call $it is denied")
//                        }
//                        // 请求失败回收当前服务
//                        "没有SD卡储存权限,下载失败".toast()
//                        intentWebDown(downloadUrl)
//                        stopSelf()
//                    }
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun addCallback(callback: UpdateFactory.ICallbackResult) {
            this@UpdateService.callback = callback
        }
    }


    private fun startDownload(downloadUrl: String, saveFileName: String) {
        //获得系统下载器
        dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //设置下载地址
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
        //设置下载文件的类型
        request.setMimeType("application/vnd.android.package-archive")
        //设置下载存放的文件夹和文件名字
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, saveFileName)
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(Global.get()?.appName)
        //执行下载，并返回任务唯一id
        enqueue = dm.enqueue(request)
    }

    fun intentWebDown(downloadUrl: String) {
        val uri = Uri.parse(downloadUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    fun install(saveFileName: String) {
        try {
            val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), saveFileName)
            val intent = Intent(Intent.ACTION_VIEW)
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                val apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".udeskfileprovider", file)
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive")
            }
            mContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
