package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.scribd.android.mocker.apimock.model.HTTPLog
import com.scribd.android.mocker.apimock.model.HTTPRequest
import com.scribd.android.mocker.apimock.model.HTTPResponse
import com.scribd.android.mocker.utils.formatJsonString

@Composable
fun HTTPLogDetail(
    modifier: Modifier = Modifier,
    httpLog: HTTPLog,
    mockClicked: () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Request", "Response")

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            //edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                RequestDetail(httpRequest = httpLog.httpRequest)
            }
            1 -> {
                ResponseDetail(httpLog.httpResponse, mockClicked)
            }
            else -> {
                val text = "Unknown tab"
                Text(text = text, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun RequestDetail(httpRequest: HTTPRequest) {
    val requestItems = listOf(
        "Method: ${httpRequest.method}",
        "URL: ${httpRequest.url}",
    )
    val headers = httpRequest.headers.keys.map { key ->
        val value = httpRequest.headers.getValue(key)
        "$key: $value"
    }
    LazyColumn(
        modifier = Modifier
            .padding(16.dp),
    ) {
        items(requestItems + headers + httpRequest.body.toString()) {
            Text(text = it, modifier = Modifier.padding(vertical = 8.dp))
            HorizontalDivider()
        }
    }
}

@Composable
fun ResponseDetail(
    httpResponse: HTTPResponse,
    mockClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val body = httpResponse.body

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                ),
                onClick = {
                    mockClicked()
                }
            ) {
                Text("Mock this response")
            }
        }

        HorizontalDivider()

        Text(text = body.formatJsonString())
    }
}

@Preview
@Composable
fun PreviewHTTPLogDetail() {
    val httpLog = HTTPLog(
        httpRequest = HTTPRequest(
            method = "GET",
            url = "https://example.com",
            headers = mapOf(
                "X-Value" to "100",
                "Cache-Enabled" to "true",
            ),
            body = "This is sample request body",
        ),
        httpResponse = HTTPResponse(
            code = 200,
            message = "OK",
            headers = mapOf(
                "Content-Type" to "application/json",
                "Cache-Enabled" to "true",
            ),
            body = "This is sample response body",
        ),
    )
    HTTPLogDetail(httpLog = httpLog) { }
}