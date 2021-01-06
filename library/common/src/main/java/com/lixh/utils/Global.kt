package com.lixh.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.lixh.BuildConfig
import com.lixh.rxhttp.Observable


class Global(application: Application) : Observable() {
    var app: Application = application
    var appName: String? = null
    private var info: ApplicationInfo = application.applicationInfo
    var packageName: String = info.packageName
    var versionName: String = BuildConfig.VERSION_NAME
    @SuppressLint("MissingPermission")
    var iMei: String = apply {
        val tm = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.READ_PHONE_STATE) !== PackageManager.PERMISSION_GRANTED) {
            return@apply
        }
        iMei = tm.deviceId
    }.toString()


    override fun notifyObservers(arg: Any?) {
        setChanged()
        super.notifyObservers(arg)
    }


    override fun toString(): String {
        return "LocalAppInfo{" +
                "appName='" + appName + '\''.toString() +
                ", packageName='" + packageName + '\''.toString() +
                ", versionName='" + versionName + '\''.toString() +
                ", imei='" + iMei + '\''.toString() +
                ", versionCode=" + versionCode +
                '}'.toString()
    }

    companion object {
        @Volatile
        private var instance: Global? = null

        fun init(application: Application) =
                instance ?: synchronized(this) {
                    instance ?: Global(application).also { instance = it }
                }

        fun get(): Global? = instance

    }
}
