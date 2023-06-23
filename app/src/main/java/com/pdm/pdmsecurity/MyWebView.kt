package com.pdm.testwebview

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.util.Log
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pdm.pdmsecurity.MainActivity

class MyWebView : WebViewClient() {
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        if (handler != null) {
            handler.proceed()
        }
    }
}