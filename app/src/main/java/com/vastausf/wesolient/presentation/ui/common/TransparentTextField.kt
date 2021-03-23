package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
private fun BasicTransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = false,
    textStyle: TextStyle,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        singleLine = singleLine,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
            ) {
                innerTextField()

                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(
                            color = textStyle.color.copy(
                                alpha = .25f
                            )
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        placeholder = placeholder
    )
}

@Composable
fun TransparentNumberTextField(
    modifier: Modifier = Modifier,
    value: Int,
    placeholder: String,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        value = value.toString(),
        placeholder = placeholder,
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        enabled = enabled,
        onValueChange = {
            onValueChange(if (it.isNotBlank()) it.filter { char -> char.isDigit() }.take(2) .toInt() else 0)
        }
    )
}
