package com.vastausf.wesolient.presentation.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader

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
                    RetryOnConnectionFailure(viewModel, settings)
                }
            }
        }
    }
}

@Composable
private fun RetryOnConnectionFailure(
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
            text = stringResource(R.string.settings_retry_on_connection_failure)
        )

        Spacer(Modifier.weight(1f))

        Checkbox(
            checked = settings.retryOnConnectionFailure,
            onCheckedChange = { checked ->
                viewModel.updateSettings {
                    retryOnConnectionFailure = checked
                }
            }
        )
    }
}
