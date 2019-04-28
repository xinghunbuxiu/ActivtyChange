package com.lixh.app;

import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.lixh.setting.AppConfig;
import com.lixh.utils.LocalAppInfo;
import com.lixh.utils.SharedPreferencesUtil;

/**
 * APPLICATION
 */
public abstract class BaseApplication extends MultiDexApplication {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate ( );
        baseApplication = this;
        LocalAppInfo.init (this);
        initPrefs ( );
        initNightMode ( );
        init ( );

    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init (getApplicationContext ( ), getPackageName ( ) + "_preference", Context.MODE_PRIVATE);
    }

    protected void initNightMode() {
        boolean isNight = SharedPreferencesUtil.getInstance ( ).getBoolean (AppConfig.ISNIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static BaseApplication getAppContext() {
        return baseApplication;
    }

    public static Resources getAppResources() {
        return baseApplication.getResources ( );
    }

    @Override
    public void onTerminate() {
        super.onTerminate ( );
    }


    public abstract void init();


    /**
     * 分包
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext (base);
    }

}
