package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader
import com.vastausf.wesolient.presentation.ui.common.TransparentTextField

@Composable
fun ScopeScreen(
    viewModel: ScopeViewModel,
    navController: NavController,
    scopeUid: String
) {
    viewModel.loadScope(scopeUid)

    Scaffold(
        topBar = {
            Header(
                viewModel,
                navController,
                scopeUid
            )
        },
        content = {
            Content(viewModel)
        }
    )
}

@Composable
private fun Header(
    viewModel: ScopeViewModel,
    navController: NavController,
    scopeUid: String,
) {
    ScreenHeader(
        leftActionIcon = painterResource(R.drawable.ic_back),
        onLeftActionClick = {
            navController.popBackStack()
        },
        content = {

        }
    )
}

@Composable
private fun Content(
    viewModel: ScopeViewModel
) {
    Column {
        Chat(viewModel)
        BottomPanel(viewModel)
    }
}

@Composable
private fun ColumnScope.Chat(
    viewModel: ScopeViewModel
) {
    LazyColumn(
        modifier = Modifier
            .weight(1f)
    ) {
        viewModel.messages.value.forEach { frame ->
            item {
                Text(
                    "${frame.content} - ${frame.source.name}"
                )
            }
        }
    }
}

@Composable
private fun BottomPanel(
    viewModel: ScopeViewModel
) {
    var messageText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TransparentTextField(
                modifier = Modifier
                    .weight(1f),
                boxModifier = Modifier
                    .padding(16.dp, 8.dp),
                value = messageText,
                placeholder = stringResource(R.string.chat_message_hint)
            ) {
                messageText = it
            }

            if (messageText.isEmpty()) {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        painterResource(R.drawable.ic_send),
                        null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            } else {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        painterResource(R.drawable.ic_templates),
                        null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}
