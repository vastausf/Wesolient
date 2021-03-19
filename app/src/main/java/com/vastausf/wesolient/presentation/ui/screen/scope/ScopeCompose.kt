package com.vastausf.wesolient.presentation.ui.screen.scope

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.common.ScreenHeader

@Composable
fun ScopeScreen(
    viewModel: ScopeViewModel,
    navController: NavController,
    scopeUid: String
) {
    viewModel.loadScope(scopeUid)

    Scaffold(
        topBar = {
            Header(navController, scopeUid)
        },
        content = {
            Text("Scope $scopeUid")
        }
    )
}

@Composable
private fun Header(
    navController: NavController,
    scopeUid: String
) {
    ScreenHeader(
        leftActionIcon = painterResource(R.drawable.ic_back),
        onLeftActionClick = {
            navController.popBackStack()
        },
        content = {

        },
        rightActionIcon = painterResource(R.drawable.ic_settings),
        onRightActionClick = {
            navController.navigate("scopeSettings/$scopeUid")
        }
    )
}
