package com.scribd.android.mocker.apimock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository

class HTTPLogViewModelFactory(
    private val httpLogRepository: HTTPLogRepository // Injected dependency
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HTTPLogViewModel::class.java)) {
            return HTTPLogViewModel(httpLogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}