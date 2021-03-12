package com.vastausf.wesolient.presentation.design

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WesolientTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = WesolientLightColors,
        typography = WesolientTypography,
        shapes = WesolientShapes,
        content = content
    )
}
