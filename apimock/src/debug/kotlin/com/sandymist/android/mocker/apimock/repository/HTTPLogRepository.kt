package com.scribd.android.mocker.apimock.repository

import com.scribd.android.mocker.apimock.db.HTTPLogDatabase
import com.scribd.android.mocker.apimock.db.HTTPLogEntity
import com.scribd.android.mocker.apimock.model.HTTPLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HTTPLogRepository @Inject constructor(
    httpLogDatabase: HTTPLogDatabase,
    private val apiMockRepository: APIMockRepository,
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val httpLogDao = httpLogDatabase.httpLogDao()

    fun getHTTPLogs() = httpLogDao.get().map { it -> it.map { it.toHTTPLog() } }

    fun addHTTPLog(httpLog: HTTPLog) {
        if (apiMockRepository.recordEnabled.value) {
            scope.launch {
                httpLogDao.insert(HTTPLogEntity.fromHTTPLog(httpLog))
            }
        }
    }
}
