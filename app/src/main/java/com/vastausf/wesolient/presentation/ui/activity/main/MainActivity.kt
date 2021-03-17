package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.design.WesolientTheme
import com.vastausf.wesolient.presentation.ui.screen.scope.ScopeScreen
import com.vastausf.wesolient.presentation.ui.screen.scope.ScopeViewModel
import com.vastausf.wesolient.presentation.ui.screen.scopeSelect.ScopeSelectScreen
import com.vastausf.wesolient.presentation.ui.screen.scopeSelect.ScopeSelectViewModel
import com.vastausf.wesolient.presentation.ui.screen.settings.SettingsScreen
import com.vastausf.wesolient.presentation.ui.screen.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WesolientTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        val context = LocalContext.current

        NavHost(navController, startDestination = "scopeList") {
            composable("scopeList") {
                val scopeSelectViewModel: ScopeSelectViewModel = hiltNavGraphViewModel()

                ScopeSelectScreen(scopeSelectViewModel, navController)
            }

            composable("settings") {
                val settingsViewModel: SettingsViewModel = hiltNavGraphViewModel()

                SettingsScreen(settingsViewModel, navController)
            }

            composable("scope/{uid}") { backStackEntry ->
                val scopeNotFound = stringResource(R.string.chat_miss_scope)

                //@TODO: Think about better solution.
                //How get non-nullable value and show message on null?
                val uid = backStackEntry.arguments?.getString("uid") ?: run {
                    Toast.makeText(context, scopeNotFound, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    return@composable
                }

                val scopeViewModel: ScopeViewModel = hiltNavGraphViewModel()

                ScopeScreen(scopeViewModel, navController, uid)
            }

            composable("scopeSettings/{uid}") { backStackEntry ->
                val scopeNotFound = stringResource(R.string.chat_miss_scope)

                //@TODO: Think about better solution.
                //How get non-nullable value and show message on null?
                val uid = backStackEntry.arguments?.getString("uid") ?: run {
                    Toast.makeText(context, scopeNotFound, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    return@composable
                }

                Text("scopeSettings/$uid")
            }
        }
    }
}
