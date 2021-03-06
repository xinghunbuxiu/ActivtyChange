package com.demon.activitychange.bean;

import java.io.Serializable;

/**
 * Created by LIXH on 2019/4/22.
 * email lixhVip9@163.com
 * des
 */
public class AppInfo implements Serializable {
    //包名
    String packageName="com.tencent.mm";
    //首页启动
    String mainName="com.tencent.mm.ui.LauncherUI";
    //加载的url
    String loadUrl = "wechart/index.html";
    String scriptPath = "index.html";

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMainName() {
        return this.mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getLoadUrl() {
        return this.loadUrl;
    }

    public void setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
    }

    public String getScriptPath() {
        return this.scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }
}
