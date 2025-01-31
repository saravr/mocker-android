@file:Suppress("unused")

package com.scribd.android.mocker.utils

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Suppress("unused")
fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }

inline fun <reified T> Modifier.conditionalClickable(item: Any, crossinline onClick: (T) -> Unit) =
    if (item is T) {
        then(
            Modifier.clickable {
                onClick(item)
            },
        )
    } else {
        this
    }

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}
