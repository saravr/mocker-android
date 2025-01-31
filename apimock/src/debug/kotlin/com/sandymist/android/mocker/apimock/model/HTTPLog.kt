package com.scribd.android.mocker.apimock.model

import com.scribd.android.mocker.utils.getURLPath
import kotlinx.serialization.Serializable

@Serializable
data class HTTPLog(
    val id: Int = 0,
    val httpRequest: HTTPRequest,
    val httpResponse: HTTPResponse,
) {
    val path = httpRequest.url.getURLPath()
}
