package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(
    showBackground = true
)
@Composable
fun FilledButton(
    title: String = "Create",
    enabled: Boolean = true,
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
