package com.demon.activitychange.server;

import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.alibaba.fastjson.JSON;
import com.demon.activitychange.UiTools.GlobalView;
import com.demon.activitychange.bean.AccessibilityEventBean;
import com.demon.activitychange.bean.AppInfo;
import com.lixh.jsSdk.AccessibilityUtil;
import com.lixh.jsSdk.base.BaseAccessibilityService;
import com.lixh.jsSdk.jscrawler.JsCrawler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
public class ListeningService extends BaseAccessibilityService {
    private static final String TAG = "WindowChange";
    Map<CharSequence, AppInfo> appInfoMap = new HashMap<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        AppInfo appInfo = (AppInfo) intent.getSerializableExtra("appinfo");
        if (appInfo != null) {
            appInfoMap.put(appInfo.getPackageName(), appInfo);
            JsCrawler.getInstance().loadUrl(appInfo.getLoadUrl());
            AccessibilityUtil.JumpToOtherApp(this, appInfo.getPackageName(), appInfo.getMainName());

        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onTypeWindowStateChanged(AccessibilityEvent event) {
        super.onTypeWindowStateChanged(event);
        GlobalView.init(this).showView(event.getPackageName() + "\n" + event.getClassName());
        AccessibilityEventBean bean = new AccessibilityEventBean();
        bean.setPackageName(event.getPackageName());
        bean.setName(event.getClassName());
        bean.setText(event.getText());
        Log.e(TAG, JSON.toJSONString(bean));
        JsCrawler.getInstance().loadWebViewInterface("onPageChanged", JSON.toJSON(bean));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalView.init(this).removeView();
    }

}

