package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.RunOnce
import com.vastausf.wesolient.data.client.*
import com.vastausf.wesolient.getText
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader
import com.vastausf.wesolient.presentation.ui.common.TransparentTextField

@Composable
fun ScopeScreen(
    viewModel: ScopeViewModel,
    navController: NavController,
    scopeUid: String
) {
    RunOnce {
        viewModel.loadScope(scopeUid)
    }

    Scaffold(
        topBar = {
            Header(
                viewModel,
                navController
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
    navController: NavController
) {
    val scope = viewModel.scope.value

    ScreenHeader(
        leftActionIcon = painterResource(R.drawable.ic_back),
        onLeftActionClick = {
            navController.popBackStack()
        },
        content = {
            if (scope != null) {
                Text(scope.title)
            }
        },
        rightActionIcon = painterResource(R.drawable.ic_down),
        onRightActionClick = {

        }
    )
}

@Composable
private fun Content(
    viewModel: ScopeViewModel
) {
    Column {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            MessageList(viewModel)
        }
        BottomPanel(viewModel)
    }
}

@Composable
private fun MessageList(
    viewModel: ScopeViewModel
) {
    val messageList = viewModel.messages.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
        contentPadding = PaddingValues(0.dp, 8.dp),
        reverseLayout = true
    ) {
        items(messageList.asReversed()) { message ->
            MessageItem(message)
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
    ) {
        Column {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colors.onBackground.copy(alpha = .05f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TransparentTextField(
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    boxModifier = Modifier
                        .padding(16.dp, 8.dp),
                    value = messageText,
                    textStyle = MaterialTheme.typography.h5,
                    placeholder = stringResource(R.string.chat_message_hint)
                ) {
                    messageText = it
                }

                if (messageText.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier
                            .size(48.dp),
                        onClick = {
                            viewModel.sendTextMessage(messageText)

                            messageText = ""
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
}

@Composable
private fun MessageItem(
    message: Message
) {
    when (message) {
        is ServerTextMessage -> ServerTextMessageItem(message)
        is ServerBytesMessage -> {

        }
        is SystemMessage -> SystemMessageItem(message)
        is ClientMessage -> ClientMessageItem(message)
    }
}

@Composable
private fun ServerTextMessageItem(
    message: ServerTextMessage
) {
    Row(
        modifier = Modifier
            .padding(8.dp, 0.dp, 24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colors.onBackground.copy(alpha = .05f)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = message.content
            )
        }
    }
}

@Composable
private fun SystemMessageItem(
    message: SystemMessage
) {
    Row(
        modifier = Modifier
            .padding(24.dp, 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = stringResource(message.getText()),
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun ClientMessageItem(
    message: ClientMessage
) {
    Row(
        modifier = Modifier
            .padding(24.dp, 0.dp, 8.dp, 0.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            modifier = Modifier,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colors.primary.copy(alpha = .075f)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = message.content
            )
        }
    }
}
