package com.scribd.android.mocker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.scribd.android.mocker.apimock.ui.APIMockActivity
import com.scribd.android.mocker.ui.theme.MockerTheme
import com.scribd.android.mocker.ui.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val testViewModel: TestViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val scope = rememberCoroutineScope()

            MockerTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            scrollBehavior = scrollBehavior,
                            colors = topAppBarColors(),
                            title = {
                                Text("API Mocks")
                            },
                            actions = {
//                                if (navController.isAtStart()) {
//                                    IconButton(onClick = { }) {
//                                        Icon(
//                                            imageVector = Icons.Default.Notifications,
//                                            contentDescription = "Notifications",
//                                        )
//                                    }
//                                }
                            },
                        )
                    }
                ) { innerPadding ->
                    val userItems by testViewModel.users.collectAsState()
                    val account by testViewModel.account.collectAsState()

                    MocksDemoScreen(
                        modifier = Modifier.padding(innerPadding),
                        userItems = userItems,
                        account = account,
                        refresh = {
                            scope.launch {
                                testViewModel.refresh()
                            }
                        },
                        launchMockActivity = {
                            APIMockActivity.launchActivity(this)
                        },
                    )
                }
            }
        }
    }
}

