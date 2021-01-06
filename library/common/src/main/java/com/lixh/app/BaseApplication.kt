package com.lixh.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.lixh.setting.AppConfig
import com.lixh.utils.Global
import com.lixh.utils.UStore
import kotlin.properties.Delegates

/**
 * APPLICATION
 */
abstract class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Global.init(this)
        initNightMode()
        init()

    }

    open fun initNightMode() {
        var isNight by UStore(AppConfig.ISNIGHT, false)
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    abstract fun init()


    companion object {

        var instance: BaseApplication by Delegates.notNull()

        val appContext: Context
            get() = instance.applicationContext

    }

}
