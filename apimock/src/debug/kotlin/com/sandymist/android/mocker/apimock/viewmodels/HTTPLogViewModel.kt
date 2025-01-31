package com.scribd.android.mocker.apimock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scribd.android.mocker.apimock.model.HTTPLog
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HTTPLogViewModel @Inject constructor (
    httpLogRepository: HTTPLogRepository,
): ViewModel() {
    val httpLogItems: StateFlow<List<HTTPLog>> = httpLogRepository.getHTTPLogs()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun getHTTPLog(id: Int): HTTPLog? {
        return httpLogItems.value.find { it.id == id } // TODO: read from DB
    }
}
