package com.demon.activitychange.app;

import android.app.Application;

import com.lixh.jsSdk.jscrawler.JsCrawler;

/**
 * Created by LIXH on 2019/4/22.
 * email lixhVip9@163.com
 * des
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JsCrawler.initialize(this);
    }
}
