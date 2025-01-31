package com.scribd.android.mocker.apimock.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.scribd.android.mocker.apimock.model.HTTPLog
import com.scribd.android.mocker.apimock.model.HTTPRequest
import com.scribd.android.mocker.apimock.model.HTTPResponse

@Entity(tableName = "http_log_table")
data class HTTPLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val request: HTTPRequest,
    val response: HTTPResponse,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toHTTPLog() = HTTPLog(id, request, response)

    companion object {
        fun fromHTTPLog(httpLog: HTTPLog) = HTTPLogEntity(
            id = httpLog.id,
            request = httpLog.httpRequest,
            response = httpLog.httpResponse,
        )
    }
}
