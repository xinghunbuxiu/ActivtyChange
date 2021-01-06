package com.lixh.jsSdk.jscrawler;

import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by LIXH on 2019/3/21.
 * email lixhVip9@163.com
 * des
 */

public class XPosedEngine {
    XC_LoadPackage.LoadPackageParam paramLoadPackageParam;
    ClassLoader loader;
    private final String TAG = "XPosedEngine";
    private static volatile XPosedEngine sInst = null;
    private ApplicationInfo appInfo;
    private String packageName;
    private String processName;


    public XPosedEngine(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        handleLoadPackage(paramLoadPackageParam);
    }

    public static XPosedEngine init(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        XPosedEngine inst = sInst;
        if (inst == null) {
            synchronized (XPosedEngine.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new XPosedEngine(paramLoadPackageParam);
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        this.paramLoadPackageParam = paramLoadPackageParam;
        this.loader = paramLoadPackageParam.classLoader;
        this.appInfo = paramLoadPackageParam.appInfo;
        this.packageName = paramLoadPackageParam.packageName;
        this.processName = paramLoadPackageParam.processName;
        JsCrawler.getInstance().loadWebViewInterface("handleLoadPackage", paramLoadPackageParam);
    }

    public XC_LoadPackage.LoadPackageParam getParamLoadPackageParam() {
        return paramLoadPackageParam;
    }

    public void setParamLoadPackageParam(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {
        this.paramLoadPackageParam = paramLoadPackageParam;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }


    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public String getProcessName() {
        return processName;
    }

    @JavascriptInterface
    public Object callMethod(Object obj, String methodName, Object... args) {
        return XposedHelpers.callMethod(obj, methodName, args);
    }


    @JavascriptInterface
    public void printLog(String msg) {
        Log.i(TAG, msg);
    }

}
