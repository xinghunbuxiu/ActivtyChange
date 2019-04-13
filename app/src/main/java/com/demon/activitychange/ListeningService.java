package com.demon.activitychange;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.demon.UiTools.GlobalView;

/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
public class ListeningService extends BaseAccessibilityService {
    private static final String TAG = "WindowChange";
    private String packageName = "com.p2peye.com.remember";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("onAccessibilityEvent", "onAccessibilityEvent");
        AccessibilityNodeInfo nodeInfo = event.getSource();//当前界面的可访问节点信息
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                GlobalView.init(this).showView(event.getPackageName() + "\n" + event.getClassName());
                if (event.getPackageName().equals(packageName)) {
                }
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalView.init(this).removeView();
    }
}

