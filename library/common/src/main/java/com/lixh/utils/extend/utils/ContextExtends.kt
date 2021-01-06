package com.lixh.utils.extend.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AlertDialog


//----------NetWork----------

/**
 * 打开网络设置
 */
fun Context.openWirelessSettings() {
    startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

/**
 * 网络是否连接
 */
fun Context.isConnected(): Boolean {
    var info = this.getActiveNetworkInfo()
    return info.isConnected
}

/**
 * 判断网络是否是移动数据
 */
fun Context.isMobileData(): Boolean {
    val info = this.getActiveNetworkInfo()
    return (null != info
            && info.isAvailable
            && info.type == ConnectivityManager.TYPE_MOBILE)
}

/**
 * 退回到桌面
 */
fun Context.startHomeActivity() {
    val homeIntent = Intent(Intent.ACTION_MAIN)
    homeIntent.addCategory(Intent.CATEGORY_HOME)
    startActivity(homeIntent)
}


@SuppressLint("MissingPermission")
private fun Context.getActiveNetworkInfo(): NetworkInfo {
    val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo
}

fun Context.permissionDialog(content: String) {
    val deleteDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage(content)
            .setPositiveButton("去设置"
            ) { _, _ -> startSettingIntent() }.create()
    deleteDialog.show()
}

/**
 * 启动app设置授权界面
 * @param context
 */
@SuppressLint("ObsoleteSdkInt")
fun Context.startSettingIntent() {
    val localIntent = Intent()
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (SDK_INT >= 9) {
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", packageName, null)
    } else if (SDK_INT <= 8) {
        localIntent.action = Intent.ACTION_VIEW
        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
        localIntent.putExtra("com.android.settings.ApplicationPkgName", packageName)
    }
    startActivity(localIntent)
}
