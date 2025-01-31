@file:Suppress("unused")

package com.scribd.android.mocker.utils

import com.scribd.android.mocker.config.AppConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class StringList(private val items: List<String>) {
    fun asList(): List<String> = items

    fun encode(): String = Json.encodeToString(this)

    fun contains(value: String) = items.contains(value)

    fun minus(value: String): StringList {
        val updated = items.minus(value)
        return StringList(updated)
    }

    fun add(value: String): StringList {
        val updatedItems = if (items.size < AppConfig.DEFAULT_MAX_RECENT_SEARCHES) {
            items
        } else {
            items.take(
                AppConfig.DEFAULT_MAX_RECENT_SEARCHES - 1,
            )
        }
        return StringList(listOf(value).plus(updatedItems))
    }

    companion object {
        private val EMPTY = StringList(emptyList())

        fun decode(value: String) = if (value.isNotBlank()) Json.decodeFromString<StringList>(value) else EMPTY
    }
}
