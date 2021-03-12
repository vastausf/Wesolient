package com.vastausf.wesolient.presentation.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WesolientTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) WesolientDarkColors else WesolientLightColors,
        typography = WesolientTypography,
        shapes = WesolientShapes,
        content = content
    )
}
