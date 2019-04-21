package com.demon.activitychange;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.demon.UiTools.GlobalView;
import com.lixh.jsSdk.base.BaseAccessibilityService;

/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
public class ListeningService extends BaseAccessibilityService {
    private static final String TAG = "WindowChange";
    private String packageName = "com.p2peye.com.remember";

    @Override
    public void init(AccessibilityEvent event) {
        Log.e ("onAccessibilityEvent", "onAccessibilityEvent");
        switch (event.getEventType ( )) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                GlobalView.init (this).showView (event.getPackageName ( ) + "\n" + event.getClassName ( ));
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy ( );
        GlobalView.init (this).removeView ( );
    }
}

