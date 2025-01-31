package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribd.android.mocker.apimock.model.HTTPLog
import com.scribd.android.mocker.apimock.model.HTTPRequest
import com.scribd.android.mocker.utils.debouncedClickable

const val NEW_MOCK_NAME_INDICATOR = "<<new>>"

@Composable
fun AddMockScreen(
    modifier: Modifier = Modifier,
    httpLogList: List<HTTPLog>,
    recordEnabled: Boolean = false,
    setRecordEnabled: (Boolean) -> Unit = {},
    navigateToDetail: (Int, String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Enable recording", style = MaterialTheme.typography.titleLarge)
            IconButton(
                onClick = {
                    setRecordEnabled(!recordEnabled)
                },
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Filled.FiberManualRecord,
                    tint = if (recordEnabled) Color.Red else Color.Gray,
                    contentDescription = "Record start/stop",
                )
            }
        }
        HorizontalDivider()
        HTTPLogs(httpLogList, navigateToDetail)
    }
}

@Composable
fun HTTPLogs(
    httpLogList: List<HTTPLog>,
    navigateToDetail: (Int, String) -> Unit,
) {
    var searchString by remember { mutableStateOf("") }

    if (httpLogList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No HTTP log items", style = MaterialTheme.typography.titleLarge)
        }
        return
    }

    Column {
        TextField(
            value = searchString,
            onValueChange = {
                searchString = it
            },
            label = { Text("Search ...") },
            trailingIcon = {
                IconButton(onClick = { searchString = "" }) {
                    Icons.Default.Clear
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        LazyColumn(
            modifier = Modifier.padding(top = 8.dp),
        ) {
            items(httpLogList.filter { it.path.contains(searchString, true) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .debouncedClickable {
                            navigateToDetail(
                                it.id,
                                NEW_MOCK_NAME_INDICATOR
                            ) // TODO - handle new mock
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = it.httpResponse.code.toString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Column {
                        val request = "${it.httpRequest.method} ${it.httpRequest.url}"
                        Text(text = request, maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                }
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    val testHTTPLogList = listOf(
        HTTPLog(
            id = 1,
            httpRequest = HTTPRequest(
                method = "GET",
                url = "https://example.com/",
                headers = emptyMap(),
                body = "Hello World!",
            ),
            httpResponse = com.scribd.android.mocker.apimock.model.HTTPResponse(
                code = 200,
                body = "Hello World!",
                headers = emptyMap(),
                message = "This is a sample response body"
            ),
        ),
        HTTPLog(
            id = 2,
            httpRequest = HTTPRequest(
                method = "GET",
                url = "https://example.com/",
                headers = emptyMap(),
                body = "Hello World!",
            ),
            httpResponse = com.scribd.android.mocker.apimock.model.HTTPResponse(
                code = 200,
                body = "Hello World!",
                headers = emptyMap(),
                message = "This is a sample response body"
            ),
        )
    )

    AddMockScreen(
        httpLogList = testHTTPLogList,
        recordEnabled = true,
        navigateToDetail = {
            _, _ ->
        },
    )
}
