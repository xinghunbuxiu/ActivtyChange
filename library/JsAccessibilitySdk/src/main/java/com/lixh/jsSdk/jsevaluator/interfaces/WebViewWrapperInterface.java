package com.lixh.jsSdk.jsevaluator.interfaces;

import android.webkit.WebView;

public interface WebViewWrapperInterface {
    void loadJavaScript(String javascript);

    void loadUrl(String url);

    // Destroys the web view in order to free the memory.
    // The web view can not be accessed after is has been destroyed.
    void destroy();

    // Returns the WebView object
    WebView getWebView();
}
