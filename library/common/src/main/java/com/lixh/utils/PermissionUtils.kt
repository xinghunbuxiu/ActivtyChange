package com.lixh.utils

import android.Manifest.permission


object PermissionUtils {
    val CAMERA = arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE, permission.CAMERA)
    val EXTERNAL_GROUP = arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
    val AllPERMISSIONS = arrayOf("android.permission.INSTALL_PACKAGES", "android.permission.DELETE_PACKAGES", "android.permission.CLEAR_APP_USER_DATA", "android.permission.ACCESS_CACHE_FILESYSTEM", "android.permission.READ_OWNER_DATA", "android.permission.WRITE_OWNER_DATA", "android.permission.CHANGE_CONFIGURATION", "android.permission.DEVICE_POWER", "android.permission.BATTERY_STATS", "android.permission.ACCESS_DOWNLOAD_MANAGER")
    val PERMISSIONS = arrayOf(permission.READ_PHONE_STATE, permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)

}