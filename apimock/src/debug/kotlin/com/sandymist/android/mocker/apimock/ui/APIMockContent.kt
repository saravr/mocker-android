package com.scribd.android.mocker.apimock.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scribd.android.mocker.apimock.viewmodels.APIMockViewModel
import com.scribd.android.mocker.apimock.viewmodels.HTTPLogViewModel
import com.scribd.android.mocker.utils.formatJsonString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun APIMockContent(
    modifier: Modifier = Modifier,
    httpLogViewModel: HTTPLogViewModel,
    apiMockViewModel: APIMockViewModel,
) {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val mockEnabled by apiMockViewModel.apiMockEnabled.collectAsState()
    val recordEnabled by apiMockViewModel.recordEnabled.collectAsState()
    val mockList by apiMockViewModel.mockList.collectAsState()
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize()) {
            val httpLogItems by httpLogViewModel.httpLogItems.collectAsState()

            Scaffold(
                modifier = modifier
                    .fillMaxSize(),
                topBar = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(),
                        title = {
                            Text("API Mocks")
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch {
                                    navController.navigate("add-mock")
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Notifications",
                                )
                            }
                        },
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    composable("home") {
                        MocksScreen(
                            modifier = modifier.padding(innerPadding),
                            mockList = mockList,
                            mockEnabled = mockEnabled,
                            setMockEnabled = {
                                apiMockViewModel.toggleAPIMockEnabled()
                            },
                            setMockPartial = { mock, status ->
                                apiMockViewModel.setMockPartial(mock, status)
                            },
//                            addMockClicked = {
//                                navController.navigate("add-mock")
//                            },
                            onToggled = { item ->
                                apiMockViewModel.toggleMock(item.id, !item.enabled)
                            },
                            navigateToDetail = {
                                val mockId = it.id
                                navController.navigate("editMock/$mockId")
                            }
                        )
                    }
                    composable("add-mock") {
                        AddMockScreen(
                            modifier = modifier.padding(innerPadding),
                            httpLogList = httpLogItems,
                            recordEnabled = recordEnabled,
                            setRecordEnabled = {
                                apiMockViewModel.toggleRecordEnabled()
                            },
                            navigateToDetail = { id, name ->
                                navController.navigate("detail/$id/$name")
                            }
                        )
                    }
                    composable("detail/{logId}/{name}") {
                        val logId = it.arguments?.getString("logId")?.toIntOrNull() ?: -1
                        val httpLog = httpLogViewModel.getHTTPLog(logId)
                        val name = it.arguments?.getString("name") ?: "" // TODO: error

                        httpLog?.let {
                            HTTPLogDetail(
                                modifier = modifier.padding(innerPadding),
                                httpLog = httpLog,
                                mockClicked = {
                                    navController.navigate("editResponse/$logId/$name")
                                }
                            )
                        } ?: run {
                            Text("Invalid/empty HTTP log")
                        }
                    }

                    composable("editMock/{mockId}") {
                        val mockId = it.arguments?.getString("mockId") ?: ""
                        val mock = apiMockViewModel.getMock(mockId)

                        mock?.let {
                            val path = mock.path
                            val body = mock.payload
                            ResponseEditorScreen(
                                modifier = modifier.padding(innerPadding),
                                id = mock.id,
                                name = mock.name,
                                body,
                                bodyUpdated = { mockId, name, editedBody ->
                                    apiMockViewModel.setMockingEnabled(
                                        id = mockId,
                                        name = name,
                                        method = mock.method,
                                        path = path,
                                        payload = editedBody,
                                        enabled = true,
                                        partial = mock.partial,
                                    )
                                },
                                goBack = {
                                    navController.popBackStack()
                                },
                            )
                        } ?: run {
                            Text("Invalid/empty path or body")
                        }
                    }

                    composable("editResponse/{logId}/{name}") {
                        val logId = it.arguments?.getString("logId")?.toIntOrNull() ?: -1
                        val httpLog = httpLogViewModel.getHTTPLog(logId)
                        val name = it.arguments?.getString("name") ?: ""

                        httpLog?.let {
                            val path = httpLog.path
                            val body = httpLog.httpResponse.body.formatJsonString()
                            ResponseEditorScreen(
                                modifier = modifier.padding(innerPadding),
                                "",
                                name = name,
                                body,
                                bodyUpdated = { mockId, name, editedBody ->
                                    apiMockViewModel.setMockingEnabled(
                                        id = mockId,
                                        name = name,
                                        method = httpLog.httpRequest.method,
                                        path = path,
                                        payload = editedBody,
                                        enabled = true,
                                        partial = false,
                                    )
                                },
                                goBack = {
                                    navController.navigate("home")
                                    //navController.popBackStack()
                                },
                            )
                        } ?: run {
                            Text("Invalid/empty path or body")
                        }
                    }
                }
            }
        }
    }
}