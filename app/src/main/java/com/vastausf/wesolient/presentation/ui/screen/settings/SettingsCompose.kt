package com.vastausf.wesolient.presentation.ui.screen.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
        },
        content = {
            Text("Settings")
        }
    )
}
