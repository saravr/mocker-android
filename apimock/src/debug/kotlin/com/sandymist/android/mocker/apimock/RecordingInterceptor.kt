package com.scribd.android.mocker.apimock

import com.scribd.android.mocker.apimock.model.HTTPLog
import com.scribd.android.mocker.apimock.model.HTTPResponse
import com.scribd.android.mocker.apimock.model.HTTPRequest
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class RecordingInterceptor @Inject constructor(
    private val httpLogRepository: HTTPLogRepository,
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val httpRequest = HTTPRequest(
            method = request.method,
            url = request.url.toString(),
            headers = request.headers.toMap(),
            body = request.body?.toString()
        )

        val response = chain.proceed(request)

        val responseBodyString = response.body?.string() ?: ""

        val httpResponse = HTTPResponse(
            code = response.code,
            message = response.message,
            headers = response.headers.toMap(),
            body = responseBodyString
        )

        httpLogRepository.addHTTPLog(
            HTTPLog(
                httpRequest = httpRequest,
                httpResponse = httpResponse,
            )
        )
//        scope.launch {
//            httpLogDao.insert(HTTPLogEntity(request = httpRequest, response = httpResponse))
//        }

        return response.newBuilder()
            .body(responseBodyString.toResponseBody(response.body?.contentType()))
            .build()
    }
}
