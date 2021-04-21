package com.vastausf.wesolient.presentation.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader
import com.vastausf.wesolient.presentation.ui.common.TransparentNumberTextField

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = {
            Header(
                navController
            )
        },
        content = {
            SettingsContent(
                viewModel
            )
        }
    )
}

@Composable
private fun Header(
    navController: NavController
) {
    ScreenHeader(
        leftActionIcon = painterResource(R.drawable.ic_back),
        onLeftActionClick = {
            navController.popBackStack()
        }
    ) {
        Text(stringResource(R.string.settings_header))
    }
}

@Composable
private fun SettingsContent(
    viewModel: SettingsViewModel
) {
    val settingsState = viewModel.settingsState.collectAsState()

    Box(
        contentAlignment = Alignment.Center
    ) {
        settingsState.value.let { settings ->
            if (settings == null) {
                CircularProgressIndicator()
            } else {
                Column {
                    ReconnectCount(viewModel, settings)
                }
            }
        }
    }
}

@Composable
private fun ReconnectCount(
    viewModel: SettingsViewModel,
    settings: Settings
) {
    Row(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.settings_reconnect_count)
        )

        Spacer(Modifier.weight(1f))

        TransparentNumberTextField(
            modifier = Modifier
                .width(64.dp)
                .padding(4.dp, 8.dp),
            value = settings.reconnectCount,
            placeholder = "0"
        ) {
            viewModel.updateSettings {
                reconnectCount = it
            }
        }
    }
}
