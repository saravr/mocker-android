package com.scribd.android.mocker.apimock.di

import android.content.Context
import com.scribd.android.mocker.apimock.APIMockInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WireMockModule {
    @Provides
    @Singleton
    fun provideAPIMockInterface(): APIMockInterface {
        return object : APIMockInterface {
            override fun init(builder: OkHttpClient.Builder) {
            }

            override fun initWithContext(context: Context, builder: OkHttpClient.Builder) {
            }

            override fun baseUrl(): String {
                return ""
            }
        }
    }
}

@Suppress("UNUSED") // Used by consumer of this library
val defaultAPIMockInterface = object : APIMockInterface {
    override fun init(builder: OkHttpClient.Builder) {
        TODO("Not yet implemented")
    }

    override fun initWithContext(context: Context, builder: OkHttpClient.Builder) {
    }

    override fun baseUrl(): String {
        return ""
    }
}
