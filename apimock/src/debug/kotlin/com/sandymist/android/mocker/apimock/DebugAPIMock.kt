package com.scribd.android.mocker.apimock

import android.content.Context
import com.github.tomakehurst.wiremock.WireMockServer
import com.scribd.android.mocker.apimock.di.wiremockPort
import com.scribd.android.mocker.apimock.interceptors.APIMockInterceptor
import com.scribd.android.mocker.apimock.interceptors.PartialBodyInterceptor
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository
import okhttp3.OkHttpClient

fun getDebugAPIMock(
    httpLogRepository: HTTPLogRepository,
    apiMockRepository: APIMockRepository,
    wireMockServer: WireMockServer,
) = object: APIMockInterface {
    override fun init(builder: OkHttpClient.Builder) {
        if (apiMockRepository.isAPIMockingEnabled()) {
            val interceptor = APIMockInterceptor(baseUrl(), apiMockRepository, wireMockServer)
            builder.addInterceptor(interceptor)
        }
        val recordingInterceptor = RecordingInterceptor(httpLogRepository)
        builder.addInterceptor(recordingInterceptor)

        val partialBodyInterceptor = PartialBodyInterceptor(apiMockRepository)
        builder.addInterceptor(partialBodyInterceptor)
    }

    override fun initWithContext(context: Context, builder: OkHttpClient.Builder) {
        TODO("Not yet implemented")
    }

    override fun baseUrl(): String {
        return "http://localhost:$wiremockPort"
    }
}