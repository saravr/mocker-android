package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchableTextField(
    text: String,
    onValueChange: (String) -> Unit,
    searchEnabled: Boolean = false,
) {
    var searchQuery by remember { mutableStateOf("") }
    var currentIndex by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val lineHeightDp = 18.dp
    val lineHeightPx = with(LocalDensity.current) { lineHeightDp.toPx() }

    val searchMatches = remember(text, searchQuery) {
        if (searchQuery.isNotEmpty()) {
            Regex(searchQuery, RegexOption.IGNORE_CASE)
                .findAll(text)
                .map { it.range.first }
                .toList()
        } else {
            emptyList()
        }
    }

    val totalMatches = searchMatches.size

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        if (searchEnabled) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    currentIndex = 0
                },
                //label = { Text("Search") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search")}
            )

            if (totalMatches > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            currentIndex = (currentIndex - 1 + totalMatches) % totalMatches
                            scrollToMatch(
                                currentIndex,
                                searchMatches,
                                scrollState,
                                coroutineScope,
                                lineHeightPx
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = "")
                    }
                    Text("${currentIndex + 1} of $totalMatches")
                    IconButton(
                        onClick = {
                            currentIndex = (currentIndex + 1) % totalMatches
                            scrollToMatch(
                                currentIndex,
                                searchMatches,
                                scrollState,
                                coroutineScope,
                                lineHeightPx
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "")
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { onValueChange(it) },
                textStyle = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(8.dp),
                visualTransformation = highlightSearchMatches(text, searchQuery, currentIndex, searchMatches)
            )
        }
    }
}

fun highlightSearchMatches(
    text: String,
    searchQuery: String,
    currentIndex: Int,
    searchMatches: List<Int>
): VisualTransformation {
    return VisualTransformation {
        val annotatedString = buildAnnotatedString {
            var lastIndex = 0

            if (searchQuery.isNotEmpty()) {
                Regex(searchQuery, RegexOption.IGNORE_CASE).findAll(text).forEach { result ->
                    val start = result.range.first
                    val end = result.range.last + 1

                    append(text.substring(lastIndex, start))

                    withStyle(style = SpanStyle(background = if (searchMatches.indexOf(start) == currentIndex) Color.Yellow else Color.LightGray)) {
                        append(text.substring(start, end))
                    }

                    lastIndex = end
                }
            }

            append(text.substring(lastIndex))
        }
        TransformedText(annotatedString, OffsetMapping.Identity)
    }
}

fun scrollToMatch(
    currentIndex: Int,
    searchMatches: List<Int>,
    scrollState: ScrollState,
    coroutineScope: CoroutineScope,
    lineHeightPx: Float,
) {
    if (searchMatches.isNotEmpty()) {
        val matchPosition = searchMatches[currentIndex]

        coroutineScope.launch {
            val offset = matchPosition * lineHeightPx
            scrollState.scrollTo((scrollState.value + 1))
            scrollState.animateScrollTo(offset.toInt())
        }
    }
}

@Preview
@Composable
fun PreviewNewTextField() {
    val testValue = """
        {
            "status": "ok",
            "code": "200",
            "version": "1.0",
            "tag": "GET/tags",
            "data": {
                "tags": [
                    {
                        "_id": "63bcf3afb8554161e99f8ea2",
                        "uid": "X53NizdxbKdsYdbnvrZfIpwa4zu2",
                        "id": "6dfee9ad-0d27-481f-8958-13b0540a13ac",
                        "name": "scala",
                        "createdAt": "1673327535.858",
                        "updatedAt": "1673327535.858",
                        "__v": 0
                    }
                ]
            }
        }
    """

    SearchableTextField(
//        modifier = Modifier
//            .fillMaxWidth(),
        text = testValue,
        onValueChange = {},
    )
}
