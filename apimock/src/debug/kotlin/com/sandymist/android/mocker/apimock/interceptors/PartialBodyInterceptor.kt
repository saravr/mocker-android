package com.scribd.android.mocker.apimock.interceptors

import com.scribd.android.mocker.apimock.db.MockEntity
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import com.scribd.android.mocker.utils.findFirstKey
import com.scribd.android.mocker.utils.getURLPath
import com.scribd.android.mocker.utils.replace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference

class PartialBodyInterceptor(
    private val apiMockRepository: APIMockRepository,
): Interceptor {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()
    private val mockList = AtomicReference<List<MockEntity>>(emptyList())

    init {
        scope.launch {
            apiMockRepository.mockList.collect { entities ->
                mutex.withLock {
                    val selectedEntities = entities.filter { it.enabled && it.partial }
                    mockList.set(selectedEntities)
                }
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url.toString()

        // TODO: protect with mutex??
        val mocks = mockList.get()
        Timber.d("Mock url $originalUrl, got ${mocks.size} mocks")

        val mock = mocks.firstOrNull { originalUrl.getURLPath().startsWith(it.path) }
            ?: run {
                return chain.proceed(chain.request())
            }
        val mockPayload = Json.parseToJsonElement(mock.payload)
        val keyToReplace = mockPayload.findFirstKey()
            ?: run {
                return chain.proceed(chain.request())
            }

        val originalResponse = chain.proceed(chain.request())
        val originalBodyString = originalResponse.body?.string() ?: ""

        val originalResponseAsJson = Json.parseToJsonElement(originalBodyString)
        val modifiedJson = originalResponseAsJson.replace(keyToReplace, mockPayload)

        val modifiedBodyString = modifiedJson.toString()
        val modifiedBody = modifiedBodyString
            .toResponseBody("application/json".toMediaTypeOrNull())

        return originalResponse.newBuilder()
            .body(modifiedBody)
            .build()
    }
}
//
//val originalString = """
//            [{
//                "id": 1,
//                "name": "Leanne Graham",
//                "username": "Bret",
//                "email": "Sincere@april.biz",
//                "address": {
//                    "street": "Kulas Light",
//                    "suite": "Apt. 556",
//                    "city": "Gwenborough",
//                    "zipcode": "92998-3874",
//                    "geo": {
//                        "lat": "-37.3159",
//                        "lng": "81.1496"
//                    }
//                },
//                "phone": "1-770-736-8031 x56442",
//                "website": "hildegard.org",
//                "company": {
//                    "name": "Romaguera-Crona",
//                    "catchPhrase": "Multi-layered client-server neural-net",
//                    "bs": "harness real-time e-markets"
//                }
//            }]
//        """.trimIndent()
//val originalJson = Json.parseToJsonElement(originalString)
//
//fun main1() {
//    val keyToReplace = "geo"
//
//    val mockPayloadString = """
//            {
//                "geo": {
//                    "lat": "-99.9999",
//                    "lng": "33.3333"
//                }
//            }
//        """.trimIndent()
//    val mockJson = Json.parseToJsonElement(mockPayloadString)
//
//    val modifiedJson = originalJson.replace(keyToReplace, mockJson)
//
//    val modifiedBodyString = modifiedJson?.toJsonString()
//
//    println(modifiedBodyString)
//}
//
//fun main() {
//    val keyToReplace = "catchPhrase"
//
//    val mockPayloadString = "This is mocked catch phrase!!!"
//
//    val modifiedJson = originalJson.replace(keyToReplace, mockPayloadString)
//
//    val modifiedBodyString = modifiedJson?.toJsonString()
//
//    println(modifiedBodyString)
//}
