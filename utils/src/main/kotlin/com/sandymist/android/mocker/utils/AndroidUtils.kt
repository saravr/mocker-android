package com.scribd.android.mocker.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebView
import timber.log.Timber

@Suppress("unused")
fun onUIThread() = (Looper.getMainLooper() == Looper.myLooper())

@SuppressLint("WebViewApiAvailability")
fun supportsWebView(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Timber.v("Webview package: " + WebView.getCurrentWebViewPackage()?.packageName)
        WebView.getCurrentWebViewPackage()?.packageName != null
    } else {
        runCatching { CookieManager.getInstance() }.isSuccess
    }
}
