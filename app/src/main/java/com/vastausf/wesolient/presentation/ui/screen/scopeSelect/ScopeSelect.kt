package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.presentation.design.EditDeleteDropdownMenu
import com.vastausf.wesolient.presentation.ui.common.AddFloatingActionButton
import com.vastausf.wesolient.presentation.ui.common.FilledButton
import com.vastausf.wesolient.presentation.ui.common.TransparentTextField

@Composable
fun ScopeSelect(
    viewModel: ScopeSelectViewModel
) {
    val scopeList = viewModel.scopeList.collectAsState()
    val createScopeDialogState = viewModel.createScopeDialog.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AddFloatingActionButton {
                viewModel.onChangeCreateScopeDialogState(true)
            }
        },
        topBar = {
            MenuHeader()
        },
    ) {
        ScopeList(viewModel, scopeList.value)
    }

    CreateScopeDialog(createScopeDialogState.value, viewModel)
}

@Composable
private fun MenuHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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

@Composable
private fun ScopeList(
    viewModel: ScopeSelectViewModel,
    scopeList: List<Scope>
) {
    val context = LocalContext.current

    Column {
        if (scopeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(scopeList) { scope ->
                    val dropdownMenuExpanded = remember { mutableStateOf(false) }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                //@TODO: Add ripple effect
                                detectTapGestures(
                                    onTap = {
                                        Toast
                                            .makeText(context, "Tap", Toast.LENGTH_SHORT)
                                            .show()
                                    },
                                    onLongPress = {
                                        dropdownMenuExpanded.value = true
                                    }
                                )
                            }
                            .padding(24.dp, 16.dp)
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = scope.title,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        EditDeleteDropdownMenu(
                            dropdownMenuExpanded,
                            onEdit = {
                                Toast.makeText(context, "onEdit ${scope.title}", Toast.LENGTH_SHORT).show()
                            },
                            onDelete = {
                                viewModel.deleteScope(scope.uid)
                            }
                        )
                    }
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
    createScopeDialogState: Boolean,
    viewModel: ScopeSelectViewModel
) {
    if (createScopeDialogState) {
        Dialog(
            onDismissRequest = {
                viewModel.onChangeCreateScopeDialogState(false)
            }
        ) {
            Surface(
                modifier = Modifier
                    .padding(8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    TransparentTextField(
                        viewModel.newScopeTitle.collectAsState().value,
                        stringResource(R.string.create_scope_title_hint)
                    ) {
                        viewModel.onNewScopeTitleChange(it)
                    }
                    TransparentTextField(
                        viewModel.newScopeUrl.collectAsState().value,
                        stringResource(R.string.create_scope_url_hint)
                    ) {
                        viewModel.onNewScopeUrlChange(it)
                    }
                    FilledButton(stringResource(R.string.create_scope_create)) {
                        viewModel.onNewScopeCreate()
                        viewModel.onChangeCreateScopeDialogState(false)
                    }
                }
            }
        }
    }
}
