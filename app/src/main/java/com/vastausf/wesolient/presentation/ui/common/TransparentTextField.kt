package com.vastausf.wesolient.presentation.ui.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
private fun BasicTransparentTextField(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = false,
    contentAlignment: Alignment = Alignment.CenterStart,
    textAlign: TextAlign = TextAlign.Start,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = TextStyle(
            textAlign = textAlign
        ),
        interactionSource = remember { MutableInteractionSource() },
        singleLine = singleLine,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = {
            Box(
                modifier = Modifier
                    .clipToBounds(),
                contentAlignment = contentAlignment
            ) {
                if (value.isNotEmpty()) {
                    Text(
                        modifier = textModifier,
                        text = value
                    )
                } else {
                    val localTextStyle = LocalTextStyle.current

                    Text(
                        modifier = textModifier,
                        text = placeholder,
                        style = localTextStyle.copy(
                            color = localTextStyle.color.copy(alpha = .25f)
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
    textModifier: Modifier,
    value: String,
    placeholder: String,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        textModifier = textModifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        placeholder = placeholder
    )
}

@Composable
fun TransparentNumberTextField(
    modifier: Modifier = Modifier,
    value: Int,
    placeholder: String,
    enabled: Boolean = true,
    onValueChange: (Int) -> Unit
) {
    BasicTransparentTextField(
        modifier = modifier,
        value = value.toString(),
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        contentAlignment = Alignment.Center,
        textAlign = TextAlign.Center,
        enabled = enabled,
        onValueChange = {
            onValueChange(if (it.isNotBlank()) it.toInt() else 0)
        }
    )
}
