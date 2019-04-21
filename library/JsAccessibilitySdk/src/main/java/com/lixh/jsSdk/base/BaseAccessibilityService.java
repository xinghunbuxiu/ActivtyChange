package com.lixh.jsSdk.base;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.lixh.jsSdk.jscrawler.JsCrawler;
import com.lixh.jsSdk.jscrawler.JsEventEngine;

/**
 * @author DeMon
 * @date 2018/8/8
 * @description
 */
public abstract class BaseAccessibilityService extends AccessibilityService {
    JsEventEngine bridge;

    public abstract void init(AccessibilityEvent accessibilityEvent);

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        bridge = JsEventEngine.init (this);
        JsCrawler.getInstance ( ).setEventEngine (bridge);
        init (accessibilityEvent);

    }

    @Override
    public void onInterrupt() {
    }

}

