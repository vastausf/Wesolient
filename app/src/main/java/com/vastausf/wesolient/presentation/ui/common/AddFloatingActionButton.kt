package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable

@Composable
fun AddFloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null
        )
    }
}
