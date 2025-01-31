package com.scribd.android.mocker.apimock.di

import android.content.Context
import android.os.Build
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.scribd.android.mocker.apimock.APIMockInterface
import com.scribd.android.mocker.apimock.RecordingInterceptor
import com.scribd.android.mocker.apimock.db.HTTPLogDatabase
import com.scribd.android.mocker.apimock.getDebugAPIMock
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository
import com.scribd.android.mocker.utils.isDebuggable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import com.google.firebase.firestore.FirebaseFirestore
import com.scribd.android.mocker.apimock.CustomResponseTransformer
import com.scribd.android.mocker.apimock.db.MockDao
import com.scribd.android.mocker.apimock.interceptors.APIMockInterceptor
import com.scribd.android.mocker.apimock.interceptors.PartialBodyInterceptor
import com.scribd.android.mocker.apimock.service.FirebaseService

const val wiremockPort = 8080

@Module
@InstallIn(SingletonComponent::class)
object WireMockModule {
    @Provides
    @Singleton
    fun provideWireMockServer(context: Context): WireMockServer? {
        return if (context.isDebuggable() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WireMockServer(
                WireMockConfiguration
                    .options()
                    .port(wiremockPort)
                    .extensions(CustomResponseTransformer())
            ).apply { start() }
        } else {
            null
        }
    }

    @Provides
    @Singleton
    fun provideAPIMockRepository(
        @ApplicationContext context: Context,
        firebaseService: FirebaseService,
        mockDao: MockDao,
    ): APIMockRepository {
        return APIMockRepository(context, firebaseService, mockDao)
    }

    @Provides
    @Singleton
    fun provideHTTPLogRepository(
        httpLogDatabase: HTTPLogDatabase,
        apiMockRepository: APIMockRepository,
    ): HTTPLogRepository {
        return HTTPLogRepository(httpLogDatabase, apiMockRepository)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(@ApplicationContext context: Context): FirebaseFirestore {
        return getMyFirestore(context)
    }

    @Provides
    @Singleton
    fun provideAPIMockInterface(
        wireMockServer: WireMockServer?,
        httpLogRepository: HTTPLogRepository,
        apiMockRepository: APIMockRepository,
    ): APIMockInterface {
        return getDebugAPIMock(httpLogRepository, apiMockRepository, wireMockServer!!)
    }
}

@Suppress("UNUSED") // Used by consumer of this library
val defaultAPIMockInterface = object : APIMockInterface {
    override fun init(builder: OkHttpClient.Builder) {
        TODO("Not yet implemented")
    }

    // TODO: use init() once host app uses DI
    override fun initWithContext(context: Context, builder: OkHttpClient.Builder) {
        val firebaseService = FirebaseService.getInstance(context)
        val httpLogDatabase = HTTPLogDatabase.getDatabase(context)
        val mockDao = httpLogDatabase.mockDao()
        val apiMockRepository = APIMockRepository(context, firebaseService, mockDao)

        val httpLogRepository = HTTPLogRepository(httpLogDatabase, apiMockRepository)

        if (apiMockRepository.isAPIMockingEnabled()) {
            val wireMockServer = WireMockModule.provideWireMockServer(context) ?: return
            val interceptor = APIMockInterceptor(baseUrl(), apiMockRepository, wireMockServer)
            builder.addInterceptor(interceptor)

            val partialBodyInterceptor = PartialBodyInterceptor(apiMockRepository)
            builder.addInterceptor(partialBodyInterceptor)
        }
        val recordingInterceptor = RecordingInterceptor(httpLogRepository)
        builder.addInterceptor(recordingInterceptor)
    }

    override fun baseUrl(): String {
        return "http://localhost:$wiremockPort"
    }
}

fun getMyFirestore(context: Context): FirebaseFirestore {
    val existingApp = FirebaseApp.getApps(context).find { it.name == "libraryFirebase" }

    if (existingApp == null) {
        val options = FirebaseOptions.Builder()
            .setApplicationId("TBD")
            .setApiKey("TBD")
            .setProjectId("TBD)
            .setStorageBucket("TBD")
            .build()

        FirebaseApp.initializeApp(context.applicationContext, options, "libraryFirebase")
    }
    val firebaseApp = FirebaseApp.getInstance("libraryFirebase")
    val firestore = FirebaseFirestore.getInstance(firebaseApp)
    return firestore
}
