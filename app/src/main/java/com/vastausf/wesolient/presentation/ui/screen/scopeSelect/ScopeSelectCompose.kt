package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.presentation.ui.common.AddFloatingActionButton
import com.vastausf.wesolient.presentation.ui.common.FilledButton
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader
import com.vastausf.wesolient.presentation.ui.common.TransparentTextField
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ScopeSelectScreen(
    viewModel: ScopeSelectViewModel,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    val scopeList = viewModel.scopeList.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    //TODO: Do something with this ugly crutch
    //https://issuetracker.google.com/issues/182966477
    if (!bottomSheetState.isVisible) {
        LocalSoftwareKeyboardController.current?.hideSoftwareKeyboard()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = MaterialTheme.shapes.large,
        sheetContent = {
            BottomSheet(
                bottomSheetState,
                viewModel
            )
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                Header(navController)
            },
            floatingActionButton = {
                AddFloatingActionButton {
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                }
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Content(navController, scopeList.value)
            }
        }
    }
}

@Composable
private fun Header(
    navController: NavController
) {
    ScreenHeader(
        rightActionIcon = painterResource(R.drawable.ic_settings),
        onRightActionClick = {
            navController.navigate("settings")
        }
    ) {
        Row(
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
}

@Composable
private fun Content(
    navController: NavController,
    scopeList: List<Scope>
) {
    Column {
        if (scopeList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                items(scopeList) { scope ->
                    ListItem(
                        title = scope.title,
                        onClick = {
                            navController.navigate("scope/${scope.uid}")
                        }
                    )
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
private fun ListItem(
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(24.dp, 16.dp),
            text = title,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheet(
    state: ModalBottomSheetState,
    viewModel: ScopeSelectViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val failureString = stringResource(R.string.create_scope_failure)

    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    val lockFields = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .padding(16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            TransparentTextField(
                value = title,
                enabled = !lockFields.value,
                placeholder = stringResource(R.string.create_scope_title_hint),
                onValueChange = {
                    title = it
                }
            )

            TransparentTextField(
                value = url,
                enabled = !lockFields.value,
                placeholder = stringResource(R.string.create_scope_url_hint),
                onValueChange = {
                    url = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FilledButton(
                    title = stringResource(R.string.create_scope_create),
                    enabled = title.isNotEmpty() && url.isNotEmpty() && !lockFields.value,
                    onClick = {
                        lockFields.value = true

                        viewModel.onScopeCreate(
                            title = title,
                            url = url,
                            onSuccess = {
                                coroutineScope.launch {
                                    state.hide()
                                }

                                title = ""
                                url = ""
                                lockFields.value = false
                            },
                            onFailure = {
                                Toast.makeText(context, failureString, Toast.LENGTH_SHORT).show()

                                lockFields.value = false
                            }
                        )
                    }
                )
            }
        }
    }
}
