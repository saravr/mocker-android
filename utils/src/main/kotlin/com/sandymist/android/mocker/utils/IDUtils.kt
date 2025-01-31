package com.scribd.android.mocker.utils

import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun generateShortUniqueId(length: Int = 8): String {
    val random = SecureRandom()
    val bytes = ByteArray(6)
    random.nextBytes(bytes)
    return Base64.encode(bytes)
        .take(length)
}
