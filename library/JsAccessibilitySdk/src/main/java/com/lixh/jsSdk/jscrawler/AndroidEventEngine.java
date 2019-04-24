package com.lixh.jsSdk.jscrawler;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.JavascriptInterface;

import com.lixh.jsSdk.base.BaseAccessibilityService;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;

/**
 * Created by LIXH on 2019/3/21.
 * email lixhVip9@163.com
 * des
 */
@Keep
public class AndroidEventEngine {


    private static final String TAG = "AndroidEventEngine";
    private static volatile AndroidEventEngine sInst = null;
    private AccessibilityManager mAccessibilityManager;
    private BaseAccessibilityService service;
    public static AndroidEventEngine init(BaseAccessibilityService service) {
        AndroidEventEngine inst = sInst;
        if (inst == null) {
            synchronized (AndroidEventEngine.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new AndroidEventEngine(service);
                    sInst = inst;
                }
            }
        }
        return inst;
    }

    public AndroidEventEngine(BaseAccessibilityService service) {
        this.service = service;
        mAccessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    @JavascriptInterface
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    /**
     * 模拟返回操作
     */
    @JavascriptInterface
    public void performBackClick() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(GLOBAL_ACTION_BACK);
    }

    /**
     * 模拟下滑操作
     */
    @JavascriptInterface
    public void performScrollBackward() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }

    /**
     * 模拟上滑操作
     */
    @JavascriptInterface
    public void performScrollForward() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    @JavascriptInterface
    public AccessibilityNodeInfo findViewByText(String text) {
        return findViewByText(text, false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @JavascriptInterface
    public AccessibilityNodeInfo findViewByNodeIndex(String id, int index) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        accessibilityNodeInfo.refresh();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            AccessibilityNodeInfo info = nodeInfoList.get(0);
            if (info.getChildCount() > index) {
                return info.getChild(index);
            }
        }
        return null;

    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    @JavascriptInterface
    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        accessibilityNodeInfo.refresh();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
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
    @JavascriptInterface
    public AccessibilityNodeInfo findViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        accessibilityNodeInfo.refresh();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @JavascriptInterface
    public void clickTextViewByText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        accessibilityNodeInfo.refresh();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @JavascriptInterface
    public void clickTextViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = service.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        accessibilityNodeInfo.refresh();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {

                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
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
    @JavascriptInterface
    public void inputText(AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }


    @JavascriptInterface
    public void printLog(String msg) {
        Log.e(TAG, msg);
    }

}
