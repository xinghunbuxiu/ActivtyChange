package com.lixh.utils

import android.app.Application
import android.content.pm.ApplicationInfo
import com.lixh.BuildConfig


class Global(application: Application) {
    var app: Application = application
    var appName: String? = null
    private var info: ApplicationInfo = application.applicationInfo
    var packageName: String = info.packageName
    var versionName: String = BuildConfig.VERSION_NAME

    override fun toString(): String {
        return "LocalAppInfo{" +
                "appName='" + appName + '\''.toString() +
                ", packageName='" + packageName + '\''.toString() +
                ", versionName='" + versionName + '\''.toString() +
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
