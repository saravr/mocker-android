package com.scribd.android.mocker.apimock.model

import kotlinx.serialization.Serializable

@Serializable
data class HTTPRequest(
    val method: String,
    val url: String,
    val headers: Map<String, String>,
    val body: String?,
)
