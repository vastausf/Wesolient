package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.presentation.design.EditDeleteDropdownMenu
import com.vastausf.wesolient.presentation.ui.common.AddFloatingActionButton
import com.vastausf.wesolient.presentation.ui.common.ScopeDialog
import com.vastausf.wesolient.presentation.ui.common.ScopeDialogType
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader

@Composable
fun ScopeSelectScreen(
    viewModel: ScopeSelectViewModel,
    navController: NavController
) {
    val scopeList = viewModel.scopeList.collectAsState()
    val createScopeDialogState = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AddFloatingActionButton {
                createScopeDialogState.value = true
            }
        },
        topBar = {
            MenuHeader(navController)
        },
    ) {
        ScopeList(viewModel, navController, scopeList.value)
    }

    CreateScopeDialog(createScopeDialogState, viewModel)
}

@Composable
private fun MenuHeader(
    navController: NavController
) {
    ScreenHeader(
        rightActionIcon = painterResource(R.drawable.ic_settings),
        onRightActionClick = {
            navController.navigate("settings")
        }
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp, 32.dp),
            painter = painterResource(R.drawable.ic_app),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Text(
            text = stringResource(R.string.app_head_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScopeList(
    viewModel: ScopeSelectViewModel,
    navController: NavController,
    scopeList: List<Scope>
) {
    Column {
        if (scopeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(scopeList) { scope ->
                    val dropdownMenuExpanded = remember { mutableStateOf(false) }

                    val editScopeDialogState = remember { mutableStateOf(false) }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            //@TODO: Watch combinedClickable experimental state
                            .combinedClickable(
                                onLongClick = {
                                    dropdownMenuExpanded.value = true
                                },
                                onClick = {
                                    navController.navigate("scope/${scope.uid}")
                                }
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(24.dp, 16.dp),
                            text = scope.title,
                            style = MaterialTheme.typography.subtitle1
                        )

                        EditDeleteDropdownMenu(
                            dropdownMenuExpanded,
                            onEdit = {
                                editScopeDialogState.value = true
                            },
                            onDelete = {
                                viewModel.deleteScope(scope.uid)
                            }
                        )
                    }

                    EditScopeDialog(editScopeDialogState, viewModel, scope)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.select_scope_list_placeholder),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
private fun CreateScopeDialog(
    state: MutableState<Boolean>,
    viewModel: ScopeSelectViewModel
) {
    if (state.value) {
        ScopeDialog(
            type = ScopeDialogType.CREATE,
            onDismiss = {
                state.value = false
            },
            onAction = { title, url ->
                viewModel.onScopeCreate(title, url)
                state.value = false
            }
        )
    }
}

@Composable
private fun EditScopeDialog(
    state: MutableState<Boolean>,
    viewModel: ScopeSelectViewModel,
    scope: Scope
) {
    if (state.value) {
        ScopeDialog(
            type = ScopeDialogType.EDIT,
            titleValue = scope.title,
            urlValue = scope.url,
            onDismiss = {
                state.value = false
            },
            onAction = { title, url ->
                viewModel.onScopeEdit(scope.uid, title, url)
                state.value = false
            }
        )
    }
}
