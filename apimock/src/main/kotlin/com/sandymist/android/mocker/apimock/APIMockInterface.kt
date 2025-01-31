package com.scribd.android.mocker.apimock

import android.content.Context
import okhttp3.OkHttpClient

interface APIMockInterface {
    fun init(builder: OkHttpClient.Builder)
    fun initWithContext(context: Context, builder: OkHttpClient.Builder)
    fun baseUrl(): String
}
