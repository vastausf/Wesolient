package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TransparentTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = Color.Unspecified,
            errorBorderColor = Color.Unspecified,
            focusedBorderColor = Color.Unspecified,
            unfocusedBorderColor = Color.Unspecified
        ),
        placeholder = {
            Text(placeholder)
        }
    )
}
