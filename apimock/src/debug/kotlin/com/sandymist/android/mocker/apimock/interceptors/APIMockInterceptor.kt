package com.scribd.android.mocker.apimock.interceptors

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import com.scribd.android.mocker.utils.getURLPath
import com.scribd.android.mocker.utils.getURLPathAndQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

class APIMockInterceptor(
    private val mockUrl: String?,
    private val apiMockRepository: APIMockRepository,
    private val wireMockServer: WireMockServer,
) : Interceptor {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()
    private val mockList = AtomicReference<List<MockEntity>>(emptyList())

    init {
        scope.launch {
            apiMockRepository.mockList.collect { entities ->
                mutex.withLock {
                    val enabledEntities = entities.filter { it.enabled && !it.partial }
                    mockList.set(enabledEntities)
                    enabledEntities.forEach {
                        setStub(wireMockServer, it.path, it.payload, it.partial)
                    }
                }
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url.toString()

        // TODO: protect with mutex??
        val mocks = mockList.get()
        Timber.d("Mock url $originalUrl, got ${mocks.size} mocks")
        val shouldUseMock = mocks.any { originalUrl.getURLPath().startsWith(it.path) }
        if (!shouldUseMock) {
            return chain.proceed(chain.request())
        }

        val newUrl = mockUrl?.let {
            val pathAndQuery = originalUrl.getURLPathAndQuery()
            originalUrl.replace(originalUrl, mockUrl + pathAndQuery)
        } ?: originalUrl

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    private fun setStub(
        wireMockServer: WireMockServer,
        path: String,
        payload: String,
        partial: Boolean,
    ) {
        val response = aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody(payload)
        val maybeTransformedResponse = if (partial) { // TODO: partial takes different route
            response
                .withTransformers("partial-body-transformer")
        } else {
            response
        }

        wireMockServer.stubFor(
            get(urlPathEqualTo(path))
                .willReturn(maybeTransformedResponse)
        )
    }
}
