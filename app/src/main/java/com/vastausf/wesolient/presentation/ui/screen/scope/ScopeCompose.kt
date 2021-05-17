package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.RunOnce
import com.vastausf.wesolient.data.client.*
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.getText
import com.vastausf.wesolient.presentation.ui.common.*

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun ScopeScreen(
    viewModel: ScopeViewModel,
    navController: NavController,
    scopeUid: String
) {
    RunOnce {
        viewModel.loadScope(scopeUid)
    }

    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val bottomMenuShownState = remember { mutableStateOf(false) }

    //Another ugly crutch with keyboard
    if (bottomMenuShownState.value) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        content = {
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
        },
        sheetContent = {
            Text("BottomSheet")
        }
    )
}

@Composable
private fun Header(
    viewModel: ScopeViewModel,
    navController: NavController
) {
    val scope = viewModel.scope.collectAsState().value
    val connectionState = viewModel.connectionState.collectAsState().value

    ScreenHeader(
        leftActionIcon = {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = null
            )
        },
        onLeftActionClick = {
            navController.popBackStack()
        },
        content = {
            if (scope != null) {
                Text(scope.title)
            }
        },
        rightActionIcon = {
            when (connectionState) {
                ConnectionState.OPENING -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp
                )
                ConnectionState.OPENED -> Icon(
                    imageVector = Icons.Rounded.Link,
                    contentDescription = null
                )
                ConnectionState.CLOSING -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp
                )
                ConnectionState.CLOSED -> Icon(
                    imageVector = Icons.Rounded.LinkOff,
                    contentDescription = null
                )
                ConnectionState.FAILED -> Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null
                )
            }
        },
        onRightActionClick = {
            if (connectionState == ConnectionState.OPENED) {
                viewModel.disconnect()
            } else if (connectionState == ConnectionState.CLOSED) {
                viewModel.connect()
            }
        },
        isRightEnabled = connectionState == ConnectionState.CLOSED
                || connectionState == ConnectionState.OPENED
    )
}

@ExperimentalMaterialApi
@Composable
private fun Content(
    viewModel: ScopeViewModel
) {
    SwipableCompose(
        primaryCompose = {
            Surface {
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
        },
        rightCompose = {
            TemplateAndVariableMenu(viewModel)
        }
    )
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

@ExperimentalMaterialApi
@Composable
private fun BottomPanel(
    viewModel: ScopeViewModel
) {
    var messageText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
    ) {
        Column {
            ThickHorizontalSpacer()

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
                    placeholder = stringResource(R.string.message_input_hint)
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
                            imageVector = Icons.Rounded.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .size(48.dp),
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AlternateEmail,
                            contentDescription = null,
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
            color = MaterialTheme.colors.onBackground.copy(alpha = .25f)
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
            color = MaterialTheme.colors.onBackground.copy(alpha = .075f)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = message.content
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TemplateAndVariableMenu(
    viewModel: ScopeViewModel
) {
    val selectedTab = remember { mutableStateOf(0) }

    Scaffold(
        content = {
            val selectedTabValue = selectedTab.value
            val scope = viewModel.scope.value

            Box(
                contentAlignment = Alignment.Center
            ) {
                if (scope != null) {
                    if (selectedTabValue == 0) {
                        TemplateList(scope.templates)
                    } else if (selectedTabValue == 1) {
                        VariableList(scope.variables)
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        },
        bottomBar = {
            ThickHorizontalSpacer()

            Row {
                BottomSheetTab(
                    selectedTab,
                    0,
                    stringResource(R.string.templates)
                )
                BottomSheetTab(
                    selectedTab,
                    1,
                    stringResource(R.string.variables)
                )
            }
        },
        floatingActionButton = {
            AddFloatingActionButton {
                //TODO: Create new template/variable
            }
        }
    )
}

@Composable
fun RowScope.BottomSheetTab(
    state: MutableState<Int>,
    index: Int,
    title: String
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clickable {
                state.value = index
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (state.value == index)
                MaterialTheme.colors.primary
            else
                MaterialTheme.colors.onBackground.copy(alpha = 0.25f)
        )
    }
}

@Composable
fun TemplateList(
    list: List<Template>
) {
    Box(
        modifier = Modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        if (list.isEmpty()) {
            PlaceholderText(stringResource(R.string.empty_template_or_variable_list_placeholder))
        } else {
            LazyColumn {
                items(list) { template ->
                    TemplateListItem(template)
                }
            }
        }
    }
}

@Composable
fun TemplateListItem(
    template: Template
) {
    Surface(
        modifier = Modifier
            .clickable {

            }
            .height(48.dp)
    ) {
        Column {
            Text(
                text = template.title,
                style = MaterialTheme.typography.h5,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = template.message,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun VariableList(
    list: List<Variable>
) {
    Box(
        modifier = Modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        if (list.isEmpty()) {
            PlaceholderText(stringResource(R.string.empty_template_or_variable_list_placeholder))
        } else {
            LazyColumn {
                items(list) { variable ->
                    VariableListItem(variable)
                }
            }
        }
    }
}

@Composable
fun VariableListItem(
    variable: Variable
) {
    Surface(
        modifier = Modifier
            .clickable {

            }
            .height(48.dp)
    ) {
        Column {
            Text(
                text = variable.title,
                style = MaterialTheme.typography.h5,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = variable.value,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
