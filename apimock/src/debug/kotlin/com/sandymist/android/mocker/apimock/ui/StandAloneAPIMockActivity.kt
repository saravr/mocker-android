package com.scribd.android.mocker.apimock.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.scribd.android.mocker.apimock.db.HTTPLogDatabase
import com.scribd.android.mocker.apimock.db.MockDao
import com.scribd.android.mocker.apimock.repository.APIMockRepository
import com.scribd.android.mocker.apimock.repository.HTTPLogRepository
import com.scribd.android.mocker.apimock.service.FirebaseService
import com.scribd.android.mocker.apimock.viewmodels.APIMockViewModel
import com.scribd.android.mocker.apimock.viewmodels.APIMockViewModelFactory
import com.scribd.android.mocker.apimock.viewmodels.HTTPLogViewModel
import com.scribd.android.mocker.apimock.viewmodels.HTTPLogViewModelFactory

class StandAloneAPIMockActivity: ComponentActivity() {
    private lateinit var httpLogViewModel: HTTPLogViewModel
    private lateinit var apiMockViewModel: APIMockViewModel

    private lateinit var mockDao: MockDao
    private lateinit var httpLogRepository: HTTPLogRepository
    private lateinit var apiMockRepository: APIMockRepository
    private lateinit var httpLogDatabase: HTTPLogDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        httpLogDatabase = HTTPLogDatabase.getDatabase(this)
        mockDao = httpLogDatabase.mockDao()

        val firebaseService = FirebaseService.getInstance(this)
        val mockDao = httpLogDatabase.mockDao()
        apiMockRepository = APIMockRepository(this, firebaseService, mockDao)

        httpLogRepository = HTTPLogRepository(httpLogDatabase, apiMockRepository)

        httpLogViewModel = ViewModelProvider(
            this,
            HTTPLogViewModelFactory(httpLogRepository)  // Pass dependencies here
        )[HTTPLogViewModel::class.java]

        apiMockViewModel = ViewModelProvider(
            this,
            APIMockViewModelFactory(apiMockRepository)
        )[APIMockViewModel::class.java]

        setContent {
            APIMockContent(
                httpLogViewModel = httpLogViewModel,
                apiMockViewModel = apiMockViewModel,
            )
        }
    }

    companion object {
        @Suppress("UNUSED")
        fun launchActivity(context: Context) {
            val intent = Intent(context, StandAloneAPIMockActivity::class.java)
            context.startActivity(intent)
        }
    }
}
