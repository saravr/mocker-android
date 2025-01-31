package com.scribd.android.mocker.apimock.service

import android.content.Context
import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.apimock.di.getMyFirestore
import com.scribd.android.mocker.apimock.model.Resource
import com.scribd.android.mocker.utils.generateShortUniqueId
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val _mockList = MutableStateFlow<Resource<List<MockEntity>>>(Resource.Loading())
    val mockList = _mockList.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val firestore = getMyFirestore(context)

    init {
        val collectionRef = firestore.collection(MOCKS_COLLECTION)

        collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Timber.e("Listen failed: ${exception.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                scope.launch {
                    val mocks = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let {
                            MockEntity(
                                id = doc.id,
                                name = doc.data?.get("name") as String,
                                method = doc.data?.get("method") as String,
                                path = doc.data?.get("path") as String,
                                payload = doc.data?.get("payload") as String,
                                enabled = doc.data?.get("enabled") as Boolean,
                                partial = (doc.data?.get("partial") as? Boolean?) ?: false, // TODO
                                createdAt = doc.data?.get("createdAt") as Long,
                            )
                        }
                    }
                    Timber.d("Fetched ${mocks.size} from firebase")
                    _mockList.emit(Resource.Success(mocks))
                }
            }
        }
    }

    fun addMock(mockEntity: MockEntity) {
        scope.launch {
            val uniqueId = generateShortUniqueId()
            val documentReference = firestore.collection(MOCKS_COLLECTION).document(uniqueId)
            documentReference.set(mockEntity.copy(id = uniqueId))
                .addOnSuccessListener {
                    Timber.e("Document added successfully with ID: $uniqueId")
                }
                .addOnFailureListener { e ->
                    Timber.e("Error adding document: ${e.message}")
                }
        }
    }

    fun setMockEnabled(id: String, enabled: Boolean) {
        scope.launch {
            val docRef = firestore.collection(MOCKS_COLLECTION).document(id)
            docRef.update("enabled", enabled)
                .addOnSuccessListener {
                    Timber.d("Document ${docRef.id} updated: enabled set to $enabled")
                }
                .addOnFailureListener { e ->
                    Timber.e("Error updating document: ${e.message}")
                }
        }
    }

    fun setMockPartial(id: String, partial: Boolean) {
        scope.launch {
            val docRef = firestore.collection(MOCKS_COLLECTION).document(id)
            docRef.update("partial", partial)
                .addOnSuccessListener {
                    Timber.d("Document ${docRef.id} updated: partial set to $partial")
                }
                .addOnFailureListener { e ->
                    Timber.e("Error updating document: ${e.message}")
                }
        }
    }

    companion object {
        private const val MOCKS_COLLECTION = "mocks"

        @Volatile
        private var instance: FirebaseService? = null

        fun getInstance(context: Context): FirebaseService {
            return instance ?: synchronized(this) {
                val newInstance = FirebaseService(context.applicationContext)
                instance = newInstance
                newInstance
            }
        }
    }
}
