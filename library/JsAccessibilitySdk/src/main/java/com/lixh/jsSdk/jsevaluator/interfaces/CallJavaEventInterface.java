package com.lixh.jsSdk.jsevaluator.interfaces;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Used in JavaScriptInterface to interact with JsRunner
 */
public interface CallJavaEventInterface {
    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    void performViewClick(AccessibilityNodeInfo nodeInfo);

    /**
     * 模拟点击事件by 文本
     */
    void clickTextViewByText(String text);

    /**
     * 模拟点击事件by id
     */
    void clickTextViewByID(String id);

    /**
     * 模拟返回操作
     */
    void performBackClick();

    /**
     * 模拟下滑操作
     */
    void performScrollBackward();

    /**
     * 模拟上滑操作
     */
    void performScrollForward();

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    AccessibilityNodeInfo findViewByText(String text);

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    AccessibilityNodeInfo findViewByText(String text, boolean clickable);

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    AccessibilityNodeInfo findViewByID(String id);

    void inputText(AccessibilityNodeInfo nodeInfo, String text);

}
