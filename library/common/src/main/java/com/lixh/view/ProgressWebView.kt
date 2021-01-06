/**
 * Copyright 2016 JustWayward Team
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lixh.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.DownloadListener
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar

import com.lixh.R

class ProgressWebView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(mContext, attrs, defStyle) {

    internal var mWebView: WebView
    internal var mProgressBar: ProgressBar
    var url: String? = null

    init {
        initView(mContext)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.layout_web_progress, this)
        mWebView = findViewById<View>(R.id.web_view) as WebView
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
    }

    fun loadUrl(url: String) {
        var url = url
        if (TextUtils.isEmpty(url)) {
            url = ""
        }
        initWebview(url)
    }


    @SuppressLint("JavascriptInterface")
    private fun initWebview(url: String) {

        mWebView.addJavascriptInterface(this, "android")

        val mWebSettings = mWebView.settings
        mWebSettings.javaScriptEnabled = true
        mWebSettings.defaultTextEncodingName = "utf-8"
        mWebSettings.cacheMode = WebSettings.LOAD_DEFAULT
        mWebSettings.pluginState = WebSettings.PluginState.ON
        mWebSettings.displayZoomControls = false
        mWebSettings.useWideViewPort = true
        mWebSettings.allowFileAccess = true
        mWebSettings.allowContentAccess = true
        mWebSettings.setSupportZoom(true)
        mWebSettings.allowContentAccess = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.builtInZoomControls = true// 隐藏缩放按钮
        mWebSettings.useWideViewPort = true// 可任意比例缩放
        mWebSettings.loadWithOverviewMode = true// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        mWebSettings.savePassword = true
        mWebSettings.saveFormData = true// 保存表单数据
        mWebSettings.javaScriptEnabled = true
        mWebSettings.textZoom = 100

        mWebSettings.domStorageEnabled = true
        mWebSettings.setSupportMultipleWindows(true)// 新加//我就是没有这一行，死活不出来。MD，硬是没有人写这一句！
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mWebSettings.mediaPlaybackRequiresUserGesture = true
        }
        if (Build.VERSION.SDK_INT >= 16) {
            mWebSettings.allowFileAccessFromFileURLs = true
            mWebSettings.allowUniversalAccessFromFileURLs = true
        }
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
        mWebSettings.setAppCacheEnabled(false)
        mWebSettings.databaseEnabled = true
        mWebSettings.setGeolocationDatabasePath(context.getDir("database", 0).path)
        mWebSettings.setGeolocationEnabled(false)
        mWebView.loadUrl(url)

        // 设置WebViewClient
        mWebView.webViewClient = object : WebViewClient() {
            // url拦截
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                view.loadUrl(url)
                // 相应完成返回true
                return true
                // return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面开始加载
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                mProgressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            // 页面加载完成
            override fun onPageFinished(view: WebView, url: String) {
                mProgressBar.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            // WebView加载的所有资源url
            override fun onLoadResource(view: WebView, url: String) {
                super.onLoadResource(view, url)
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                //				view.loadData(errorHtml, "text/html; charset=UTF-8", null);
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

        }

        // 设置WebChromeClient
        mWebView.webChromeClient = object : WebChromeClient() {
            override// 处理javascript中的alert
            fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override// 处理javascript中的confirm
            fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            override// 处理javascript中的prompt
            fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            // 设置网页加载的进度条
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                mProgressBar.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            // 设置程序的Title
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }
        }
        mWebView.setOnKeyListener(OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键

                    mWebView.goBack() // 后退

                    // webview.goForward();//前进
                    return@OnKeyListener true // 已处理
                }
            }
            false
        })
    }

    fun canBack(): Boolean {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
            return false
        }
        return true
    }
}