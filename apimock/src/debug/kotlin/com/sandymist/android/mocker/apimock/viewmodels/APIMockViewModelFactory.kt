package com.scribd.android.mocker.apimock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scribd.android.mocker.apimock.repository.APIMockRepository

class APIMockViewModelFactory(
    private val apiMockRepository: APIMockRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(APIMockViewModel::class.java)) {
            return APIMockViewModel(apiMockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}