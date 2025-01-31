package com.scribd.android.mocker.utils

import android.os.SystemClock
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.scribd.android.mocker.config.AppConfig.DEFAULT_BUTTON_DEBOUNCE_IN_MS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO(3): need a home grown version
/*
 * Courtesy: https://gist.github.com/leonardoaramaki/153b27eb5325f878ad4bb7ffe540c2ef
 */

/**
 * Wraps an [onClick] lambda with another one that supports debouncing. The default deboucing time
 * is 1000ms.
 *
 * @return debounced onClick
 */
@Composable
inline fun debounced(crossinline onClick: () -> Unit, debounceTime: Long = DEFAULT_BUTTON_DEBOUNCE_IN_MS): () -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onClickLambda: () -> Unit = {
        val now = SystemClock.uptimeMillis()
        if (now - lastTimeClicked > debounceTime) {
            onClick()
        }
        lastTimeClicked = now
    }
    return onClickLambda
}

/**
 * The same as [Modifier.clickable] with support to debouncing.
 */
fun Modifier.debouncedClickable(
    debounceTime: Long = DEFAULT_BUTTON_DEBOUNCE_IN_MS,
    onClick: () -> Unit,
): Modifier {
    return this.composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = { onClick() })
        this.clickable { clickable() }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.debouncedCombinedClickable(
    debounceTime: Long = 1000L,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
): Modifier {
    return this.composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = { onClick() })
        this.combinedClickable(onClick = clickable, onLongClick = onLongClick)
    }
}

@Composable
fun DebouncedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    debounceTime: Long = 500L,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val debounceActive = remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            if (!debounceActive.value) {
                debounceActive.value = true
                coroutineScope.launch {
                    delay(debounceTime)
                    debounceActive.value = false
                }
                onClick()
            }
        },
        modifier = modifier,
        content = content,
    )
}
