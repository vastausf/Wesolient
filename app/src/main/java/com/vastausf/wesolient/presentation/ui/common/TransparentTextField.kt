package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
private fun BasicTransparentTextField(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = false,
    textStyle: TextStyle,
    contentAlignment: Alignment,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        singleLine = singleLine,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = boxModifier,
                contentAlignment = contentAlignment
            ) {
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

                innerTextField()
            }
        }
    )
}

@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        boxModifier = boxModifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        enabled = enabled,
        textStyle = textStyle,
        contentAlignment = Alignment.CenterStart,
        keyboardOptions = keyboardOptions,
        placeholder = placeholder
    )
}

@Composable
fun TransparentNumberTextField(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier,
    value: Int,
    placeholder: String,
    textStyle: TextStyle = MaterialTheme.typography.h6,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        boxModifier = boxModifier,
        value = if (value != 0) value.toString() else "",
        placeholder = placeholder,
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center
        ),
        contentAlignment = Alignment.Center,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        enabled = enabled,
        onValueChange = {
            onValueChange(
                if (it.isNotBlank()) it.filter { char -> char.isDigit() }.take(2).toInt() else 0
            )
        }
    )
}
