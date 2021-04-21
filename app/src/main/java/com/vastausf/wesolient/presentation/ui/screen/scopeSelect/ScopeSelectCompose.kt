package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.vastausf.wesolient.R
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

    val scaffoldState = rememberScaffoldState()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    //TODO: Do something with this ugly crutch
    //https://issuetracker.google.com/issues/182966477
    if (!bottomSheetState.isVisible) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = MaterialTheme.shapes.large,
        scrimColor = Color(0xBFFFFFFF),
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
                Content(navController, viewModel)
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
        },
        haveShadow = false
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
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun Content(
    navController: NavController,
    viewModel: ScopeSelectViewModel
) {
    val scopeList = viewModel.scopeList.value

    Box(
        contentAlignment = Alignment.Center
    ) {
        when {
            scopeList == null -> {
                CircularProgressIndicator()
            }
            scopeList.isNotEmpty() -> {
                LazyColumn(
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
            }
            else -> {
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
            style = MaterialTheme.typography.h6
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

    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .padding(16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            TransparentTextField(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                value = title,
                placeholder = stringResource(R.string.create_scope_title_hint),
                onValueChange = {
                    title = it
                }
            )

            TransparentTextField(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                value = url,
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
                    enabled = title.isNotEmpty() && url.isNotEmpty(),
                    onClick = {
                        viewModel.onScopeCreate(
                            title = title,
                            url = url
                        )

                        title = ""
                        url = ""

                        coroutineScope.launch {
                            state.hide()
                        }
                    }
                )
            }
        }
    }
}
