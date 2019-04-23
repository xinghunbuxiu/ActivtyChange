package com.lixh.jsSdk.jsevaluator.interfaces;

import android.webkit.WebView;


public interface JsEvaluatorInterface {
    void callFunction(String jsCode, JsCallback resultCallback, String name, Object... args);

    void loadUrl(String url);

    void evaluate(String jsCode);

    void evaluate(String jsCode, JsCallback resultCallback);

    // Destroys the web view in order to free the memory.
    // The web view can not be accessed after is has been destroyed.
    void destroy();

    // Returns the WebView object
    WebView getWebView();
}
