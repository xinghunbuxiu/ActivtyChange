package com.lixh.jsSdk.jscrawler;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * Created by LIXH on 2019/3/21.
 * email lixhVip9@163.com
 * des
 */
public class JsEventEngine extends AndroidEventEngine {

    private static volatile JsEventEngine sInst = null;
    private AccessibilityManager mAccessibilityManager;
    private AccessibilityService service;

    public static JsEventEngine init(AccessibilityService service) {
        JsEventEngine inst = sInst;
        if (inst == null) {
            synchronized (JsEventEngine.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new JsEventEngine (service);
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public JsEventEngine(AccessibilityService service) {
        this.service = service;
        mAccessibilityManager = (AccessibilityManager) service.getSystemService (Context.ACCESSIBILITY_SERVICE);
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    @Override
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable ( )) {
                nodeInfo.performAction (AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent ( );
        }
    }

    /**
     * 模拟返回操作
     */
    @Override
    public void performBackClick() {
        try {
            Thread.sleep (500);
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
        service.performGlobalAction (GLOBAL_ACTION_BACK);
    }

    /**
     * 模拟下滑操作
     */
    @Override
    public void performScrollBackward() {
        try {
            Thread.sleep (500);
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
        service.performGlobalAction (AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    /**
     * 模拟上滑操作
     */
    @Override
    public void performScrollForward() {
        try {
            Thread.sleep (500);
        } catch (InterruptedException e) {
            e.printStackTrace ( );
        }
        service.performGlobalAction (AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    @Override
    public AccessibilityNodeInfo findViewByText(String text) {
        return findViewByText (text, false);
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    @Override
    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow ( );
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText (text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty ( )) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable ( ) == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public AccessibilityNodeInfo findViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow ( );
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId (id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty ( )) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }


    @Override
    public void clickTextViewByText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow ( );
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText (text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty ( )) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick (nodeInfo);
                    break;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void clickTextViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow ( );
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId (id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty ( )) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick (nodeInfo);
                    break;
                }
            }
        }
    }

    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    @Override
    public void inputText(AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle ( );
            arguments.putCharSequence (AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction (AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) service.getSystemService (Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText ("label", text);
            clipboard.setPrimaryClip (clip);
            nodeInfo.performAction (AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction (AccessibilityNodeInfo.ACTION_PASTE);
        }
    }


}
