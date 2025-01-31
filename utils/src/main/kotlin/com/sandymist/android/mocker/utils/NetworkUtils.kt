package com.scribd.android.mocker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.URI

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetwork?.let {
        connectivityManager.getNetworkCapabilities(it)?.hasRequiredTransport() ?: false
    } ?: false
}

fun NetworkCapabilities.hasRequiredTransport(): Boolean =
    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

fun String.getURLPath(): String {
    val uri = URI(this)
    return uri.path
}

fun String.getURLPathAndQuery(): String {
    val uri = URI(this)
    return uri.rawPath + if (uri.rawQuery != null) "?" + uri.rawQuery else ""
}
