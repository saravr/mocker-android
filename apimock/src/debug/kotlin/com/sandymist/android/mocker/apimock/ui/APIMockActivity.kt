package com.scribd.android.mocker.apimock.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.scribd.android.mocker.apimock.viewmodels.APIMockViewModel
import com.scribd.android.mocker.apimock.viewmodels.HTTPLogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class APIMockActivity: ComponentActivity() {
    private val httpLogViewModel: HTTPLogViewModel by viewModels()
    private val apiMockViewModel: APIMockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val intent = Intent(context, APIMockActivity::class.java)
            context.startActivity(intent)
        }
    }
}
