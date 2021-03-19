package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilledButton(
    title: String,
    enabled: Boolean,
    onClick: () -> Unit = {}
) {
    //@TODO: Animate enable button state changes
    Button(
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            text = title,
            lineHeight = 16.sp
        )
    }
}
