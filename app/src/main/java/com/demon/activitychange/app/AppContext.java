package com.demon.activitychange.app;

import android.app.Application;

import com.lixh.app.BaseApplication;
import com.lixh.jsSdk.jscrawler.JsCrawler;

/**
 * Created by LIXH on 2019/4/22.
 * email lixhVip9@163.com
 * des
 */
public class AppContext extends BaseApplication {

    @Override
    public void onTerminate() {
        super.onTerminate();
        JsCrawler.release();
    }

    @Override
    public void init() {
        JsCrawler.initialize(this);
    }
}
