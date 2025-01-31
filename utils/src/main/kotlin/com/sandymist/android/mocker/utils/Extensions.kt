@file:Suppress("unused")

package com.scribd.android.mocker.utils

import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Composable
fun String.ifNotEmpty(handler: @Composable (String) -> Unit) {
    if (isNotEmpty()) {
        handler(this)
    }
}

@Suppress("unused")
fun String.titleCase() = replaceFirstChar { it.uppercase() }

fun Boolean.toInt() = if (this) 1 else 0

fun String.formatJsonString(): String {
    val json = Json { prettyPrint = true }
    val jsonElement = json.parseToJsonElement(this)
    return json.encodeToString(JsonElement.serializer(), jsonElement)
}
