package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.presentation.ui.common.AddFloatingActionButton
import com.vastausf.wesolient.presentation.ui.common.ScopeDialogCompose
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
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
        topBar = {
            MenuHeader(navController)
        },
        floatingActionButton = {
            AddFloatingActionButton {
                createScopeDialogState.value = true
            }
        }
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

@OptIn(ExperimentalMaterialApi::class)
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
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                items(scopeList) { scope ->
                    val editScopeDialogState = remember { mutableStateOf(false) }

                    val swipeableState = rememberSwipeableState(0)
                    val areaWidth = with(LocalDensity.current) { 48.dp.toPx() }
                    val swipeableAnchors = mapOf(
                        -areaWidth to -1,
                        0f to 0,
                        areaWidth to 1
                    )

                    val coroutineScope = rememberCoroutineScope()

                    Surface(
                        modifier = Modifier
                            .swipeable(
                                state = swipeableState,
                                anchors = swipeableAnchors,
                                thresholds = { from, to -> FractionalThreshold(0.3f) },
                                orientation = Orientation.Horizontal
                            )
                    ) {
                        Row {
                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                IconButton(
                                    onClick = {
                                        editScopeDialogState.value = true
                                        coroutineScope.launch {
                                            swipeableState.animateTo(0)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_edit),
                                        contentDescription = null
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                IconButton(
                                    onClick = {
                                        viewModel.deleteScope(scope.uid)
                                        coroutineScope.launch {
                                            swipeableState.snapTo(0)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_delete),
                                        contentDescription = null,
                                        tint = Color(0xFFF44336)
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                                .clickable {
                                    navController.navigate("scope/${scope.uid}")
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(24.dp, 16.dp),
                                text = scope.title,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
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
    val context = LocalContext.current

    val failureString = stringResource(R.string.create_scope_failure)

    if (state.value) {
        ScopeDialogCompose(
            state = state,
            actionTitle = stringResource(R.string.create_scope_create)
        ) { title, url ->
            viewModel.onScopeCreate(
                title = title,
                url = url,
                onSuccess = {
                    state.value = false
                },
                onFailure = {
                    Toast.makeText(context, failureString, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun EditScopeDialog(
    state: MutableState<Boolean>,
    viewModel: ScopeSelectViewModel,
    scope: Scope
) {
    val context = LocalContext.current

    val failureString = stringResource(R.string.create_scope_failure)

    if (state.value) {
        ScopeDialogCompose(
            titleValue = scope.title,
            urlValue = scope.url,
            state = state,
            actionTitle = stringResource(R.string.edit_scope_apply)
        ) { title, url ->
            viewModel.onScopeEdit(
                uid = scope.uid,
                title = title,
                url = url,
                onSuccess = {
                    state.value = false
                },
                onFailure = {
                    Toast.makeText(context, failureString, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
