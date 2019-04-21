package com.lixh.jsSdk.jscrawler;

import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.JavascriptInterface;

/**
 * Used in JavaScriptInterface to interact with JsRunner
 */
public abstract class AndroidEventEngine {
    private static final String TAG = "AndroidEventEngine";

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    @JavascriptInterface
    abstract void performViewClick(AccessibilityNodeInfo nodeInfo);

    /**
     * 模拟点击事件by 文本
     */
    @JavascriptInterface
    abstract void clickTextViewByText(String text);

    /**
     * 模拟点击事件by id
     */
    @JavascriptInterface
    abstract void clickTextViewByID(String id);

    /**
     * 模拟返回操作
     */
    @JavascriptInterface
    abstract void performBackClick();

    /**
     * 模拟下滑操作
     */
    @JavascriptInterface
    abstract void performScrollBackward();

    /**
     * 模拟上滑操作
     */
    @JavascriptInterface
    abstract void performScrollForward();

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    @JavascriptInterface
    abstract AccessibilityNodeInfo findViewByText(String text);

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    @JavascriptInterface
    abstract AccessibilityNodeInfo findViewByText(String text, boolean clickable);

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @JavascriptInterface
    abstract AccessibilityNodeInfo findViewByID(String id);

    /**
     * 输入文本
     *
     * @param nodeInfo
     * @param text
     */
    @JavascriptInterface
    abstract void inputText(AccessibilityNodeInfo nodeInfo, String text);


}
