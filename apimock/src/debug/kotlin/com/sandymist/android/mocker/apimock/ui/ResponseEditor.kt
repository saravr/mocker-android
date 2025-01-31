package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ResponseEditorScreen(
    modifier: Modifier = Modifier,
    id: String,
    name: String,
    body: String,
    bodyUpdated: (String, String, String) -> Unit,
    goBack: () -> Unit,
) {
    var editedBody by remember { mutableStateOf(body) }
    var mockName by remember { mutableStateOf(if (name == NEW_MOCK_NAME_INDICATOR) "" else name) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var searchEnabled by remember { mutableStateOf(false) }
        val clipboardManager: ClipboardManager = LocalClipboardManager.current

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Response Editor",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
            )

            IconButton(
                onClick = {
                    searchEnabled = !searchEnabled
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "",
                )
            }

            IconButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString((editedBody)))
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "",
                )
            }

            IconButton(
                onClick = {
                    bodyUpdated(id, mockName, editedBody)
                    goBack()
                },
                enabled = mockName.isNotBlank(),
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "",
                )
            }
        }

        HorizontalDivider()

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = mockName,
            onValueChange = {
                mockName = it
            },
            label = { Text("Name of the mock")},
            placeholder = {
                Text("Enter a name to identify the mock")
            },
        )

        SearchableTextField(
            text = editedBody,
            onValueChange = {
                editedBody = it
            },
            searchEnabled = searchEnabled,
        )
    }
}

@Preview
@Composable
fun PreviewResponseEditorScreen() {
    ResponseEditorScreen(
        id = "1",
        name = "Test mock",
        body = "Hello World!",
        bodyUpdated = { _, _, _ ->
        },
        goBack = {},
    )
}
