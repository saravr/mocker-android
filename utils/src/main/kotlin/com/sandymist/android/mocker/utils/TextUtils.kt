package com.scribd.android.mocker.utils

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp

@Composable
fun AdaptiveText(text: String, baseSize: Int) {
    val density = LocalDensity.current.density
    val baseTextSize = baseSize.sp
    val smallDeviceTextSize = if (density <= 2.0f) {
        (baseTextSize.value * 0.8).sp
    } else {
        baseTextSize
    }
    Text(text = text, fontSize = smallDeviceTextSize)
}
