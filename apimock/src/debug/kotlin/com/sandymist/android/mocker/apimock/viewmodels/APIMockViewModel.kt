package com.scribd.android.mocker.apimock.viewmodels

import androidx.lifecycle.ViewModel
import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class APIMockViewModel @Inject constructor(
    private val apiMockRepository: APIMockRepository,
): ViewModel() {
    val apiMockEnabled = apiMockRepository.apiMockingEnabled

    val recordEnabled = apiMockRepository.recordEnabled

    fun toggleAPIMockEnabled() = apiMockRepository.toggleAPIMocking()

    fun setMockPartial(mockEntity: MockEntity, status: Boolean) = apiMockRepository.setMockPartial(mockEntity, status)

    fun toggleRecordEnabled() = apiMockRepository.toggleRecording()

    val mockList = apiMockRepository.mockList

    fun getMock(id: String) = mockList.value.find { it.id == id }

    fun setMockingEnabled(
        id: String,
        name: String,
        method: String,
        path: String,
        payload: String,
        enabled: Boolean,
        partial: Boolean,
    ) = apiMockRepository.setMockingEnabled(id, name, method, path, payload, enabled, partial)

    fun toggleMock(id: String, enabled: Boolean) = apiMockRepository.toggleMock(id, enabled)
}
