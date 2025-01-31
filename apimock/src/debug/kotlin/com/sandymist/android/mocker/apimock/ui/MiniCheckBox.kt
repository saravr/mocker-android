package com.scribd.android.mocker.apimock.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MiniCheckBox(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, modifier = Modifier.padding(end = 4.dp))
        Icon(
            imageVector = if (selected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
            contentDescription = null,
            tint = Color.Black,
            modifier = modifier
                .size(20.dp)
                .clickable {
                    onSelected(!selected)
                },
        )
    }
}

@Preview
@Composable
fun PreviewMiniCheckBox() {
    MiniCheckBox(title = "Multiple choice", selected = true) { }
}
