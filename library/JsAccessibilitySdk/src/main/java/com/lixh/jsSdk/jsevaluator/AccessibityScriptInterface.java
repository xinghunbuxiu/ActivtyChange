package com.lixh.jsSdk.jsevaluator;

import android.webkit.JavascriptInterface;

import com.lixh.jsSdk.jsevaluator.interfaces.CallJavaEventInterface;
import com.lixh.jsSdk.jsevaluator.interfaces.CallJavaResultInterface;

/**
 * Passed in addJavascriptInterface of WebView to allow web views's JS execute
 * Java code
 */
public class AccessibityScriptInterface {
    private final CallJavaEventInterface mCallJavaResultInterface;

    public AccessibityScriptInterface(CallJavaEventInterface callJavaResult) {
        mCallJavaResultInterface = callJavaResult;
    }

    @JavascriptInterface
    public void returnResultToJava(String value) {

    }
}