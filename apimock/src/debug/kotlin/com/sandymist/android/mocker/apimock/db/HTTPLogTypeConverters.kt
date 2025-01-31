package com.scribd.android.mocker.apimock.db

import androidx.room.TypeConverter
import com.scribd.android.mocker.apimock.model.HTTPRequest
import com.scribd.android.mocker.apimock.model.HTTPResponse
import kotlinx.serialization.json.Json

class HTTPLogTypeConverters {
    @TypeConverter
    fun fromRequest(httpRequest: HTTPRequest): String {
        return Json.encodeToString(HTTPRequest.serializer(), httpRequest)
    }

    @TypeConverter
    fun toRequest(requestString: String): HTTPRequest {
        return Json.decodeFromString(requestString)
    }

    @TypeConverter
    fun fromResponse(httpResponse: HTTPResponse): String {
        return Json.encodeToString(HTTPResponse.serializer(), httpResponse)
    }

    @TypeConverter
    fun toResponse(responseString: String): HTTPResponse {
        return Json.decodeFromString(responseString)
    }
}
