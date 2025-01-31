package com.scribd.android.mocker.apimock.di

import android.content.Context
import com.scribd.android.mocker.apimock.db.HTTPLogDao
import com.scribd.android.mocker.apimock.db.HTTPLogDatabase
import com.scribd.android.mocker.apimock.db.MockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): HTTPLogDatabase =
        HTTPLogDatabase.getDatabase(context)

    @Provides
    fun provideHTTPLogDao(httpLogDatabase: HTTPLogDatabase): HTTPLogDao =
        httpLogDatabase.httpLogDao()

    @Provides
    fun provideMockDao(httpLogDatabase: HTTPLogDatabase): MockDao =
        httpLogDatabase.mockDao()
}
