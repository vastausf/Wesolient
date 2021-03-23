package com.vastausf.wesolient.presentation.ui.screen.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
                viewModel,
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
    viewModel: SettingsViewModel,
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

    Column {
        Surface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_reconnect_count)
                )

                Spacer(Modifier.weight(1f))

                TransparentNumberTextField(
                    modifier = Modifier
                        .widthIn(min = 32.dp),
                    value = settingsState.value.reconnectCount,
                    placeholder = "0"
                ) {
                    viewModel.updateSettings {
                        reconnectCount = it
                    }
                }
            }
        }
    }
}
