package com.scribd.android.mocker.apimock.repository

import android.content.Context
import com.scribd.android.mocker.apimock.db.MockDao
import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.apimock.model.Resource
import com.scribd.android.mocker.apimock.service.FirebaseService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class APIMockRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseService: FirebaseService,
    private val mockDao: MockDao,
) {
    private val sharedPreferences = context.getSharedPreferences("PREF_API_MOCK", Context.MODE_PRIVATE)
    private val _apiMockingEnabled = MutableStateFlow(isAPIMockingEnabled())
    val apiMockingEnabled: StateFlow<Boolean> = _apiMockingEnabled
    private val _recordEnabled = MutableStateFlow(isRecordingEnabled())
    val recordEnabled: StateFlow<Boolean> = _recordEnabled
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _mockList = MutableStateFlow<List<MockEntity>>(emptyList())
    val mockList = _mockList.asStateFlow()

    init {
        scope.launch {
            mockDao.getMockFlow().collectLatest {
                _mockList.emit(it)
            }
        }
        scope.launch {
            firebaseService.mockList.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        mockDao.replaceAll (it.data)
                    }
                    else -> {}
                }
            }
        }
    }

//    init {
//        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
//            if (key == "PREF_KEY_API_MOCKING_ENABLED") {
//                _apiMockingEnabled.value = isAPIMockingEnabled()
//            }
//            if (key == "PREF_KEY_RECORDING_ENABLED") {
//                _recordEnabled.value = isRecordingEnabled()
//            }
//        }
//        // TODO: unregister??
//    }

    fun isAPIMockingEnabled() = sharedPreferences.getBoolean("PREF_KEY_API_MOCKING_ENABLED", false)

    fun toggleAPIMocking() {
        val enabled = isAPIMockingEnabled()
        sharedPreferences.edit().putBoolean("PREF_KEY_API_MOCKING_ENABLED", !enabled).apply()
        // TODO: use commit or listener
        _apiMockingEnabled.value = !enabled
    }

    fun setMockPartial(mockEntity: MockEntity, status: Boolean) {
        firebaseService.setMockPartial(mockEntity.id, status)
    }

    private fun isRecordingEnabled(): Boolean = sharedPreferences.getBoolean("PREF_KEY_RECORDING_ENABLED", false)

    fun toggleRecording() {
        val enabled = isRecordingEnabled()
        sharedPreferences.edit().putBoolean("PREF_KEY_RECORDING_ENABLED", !enabled).apply()
        // TODO: use commit or listener
        _recordEnabled.value = !enabled
    }

    fun setMockingEnabled(id: String, name: String, method: String, path: String, payload: String, enabled: Boolean, partial: Boolean) {
//        scope.launch {
//            val mockEntity = MockEntity(id = id, name = name, method = method, path = path, payload = payload, enabled = enabled)
//            mockDao.insert(mockEntity)
//            // TODO: mockDao.remove(path = path)
//        }

        val mockEntity = MockEntity(id = id, name = name, method = method, path = path, payload = payload, enabled = enabled, partial = partial)
        firebaseService.addMock(mockEntity)
    }

    fun toggleMock(id: String, enabled: Boolean) {
        firebaseService.setMockEnabled(id, enabled)
//        scope.launch {
//            mockDao.setEnabled(id, enabled)
//        }
    }
}
