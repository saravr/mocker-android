@file:Suppress("unused")

package com.scribd.android.mocker.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

inline fun <reified T> T?.toJsonString(): String {
    val prettyJson = Json {
        prettyPrint = true
    }

    return prettyJson.encodeToString(this)
}

fun JsonElement.findFirstKey() = (this as? JsonObject)?.keys?.firstOrNull()

fun JsonElement.findValue(targetKey: String): JsonElement? {
    when (this) {
        is JsonObject -> {
            this[targetKey]?.let { return it }

            for ((_, value) in this) {
                val result = value.findValue(targetKey)
                if (result != null) {
                    return result
                }
            }
        }
        is JsonArray -> {
            for (element in this) {
                val result = element.findValue(targetKey)
                if (result != null) {
                    return result
                }
            }
        }
        is JsonPrimitive -> {}
        else -> {}
    }
    return null
}

fun JsonElement.findParent(targetKey: String): JsonElement? {
    when (this) {
        is JsonObject -> {
            this[targetKey]?.let { return it }

            for ((_, value) in this) {
                val result = value.findValue(targetKey)
                if (result != null) {
                    return value
                }
            }
        }
        is JsonArray -> {
            for (element in this) {
                val result = this.findValue(targetKey)
                if (result != null) {
                    return this
                }
            }
        }
        is JsonPrimitive -> {}
        else -> {}
    }
    return null
}

fun JsonElement.replace(keyToReplace: String, newValue: Any): JsonElement? {
    when (this) {
        is JsonObject -> {
            if (this.containsKey(keyToReplace)) {
                val updatedMap = this.toMutableMap().apply {
                    val replacedValue = when (newValue) {
                        is JsonObject -> JsonObject(newValue)
                        is String -> JsonPrimitive(newValue)
                        is Int -> JsonPrimitive(newValue)
                        is Long -> JsonPrimitive(newValue)
                        is Float -> JsonPrimitive(newValue)
                        is Double -> JsonPrimitive(newValue)
                        else -> throw Exception("Unknown type")
                    }
                    this[keyToReplace] = when (replacedValue) {
                        is JsonObject ->
                            replacedValue.jsonObject.values.first()
                        else -> replacedValue
                    }
                }
                return JsonObject(updatedMap)
            }

            val updatedMap = this.toMutableMap()
            for ((key, value) in this) {
                value.replace(keyToReplace, newValue)?.let {
                    updatedMap[key] = it
                }
            }
            return JsonObject(updatedMap)
        }
        is JsonArray -> {
            val updatedList = mutableListOf<JsonElement>()
            for (element in this) {
                element.jsonObject.replace(keyToReplace, newValue)?.let {
                    updatedList.add(it)
                }
            }
            return JsonArray(updatedList)
        }
        is JsonPrimitive -> {}
        else -> {}
    }
    return null
}
