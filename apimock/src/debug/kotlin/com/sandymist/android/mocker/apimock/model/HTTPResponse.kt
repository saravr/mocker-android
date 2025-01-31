package com.scribd.android.mocker.apimock.model

import kotlinx.serialization.Serializable

@Serializable
data class HTTPResponse(
    val code: Int,
    val message: String,
    val headers: Map<String, String>,
    val body: String
)