package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vastausf.wesolient.presentation.design.WesolientTheme
import com.vastausf.wesolient.presentation.ui.fragment.chat.ChatViewModel
import com.vastausf.wesolient.presentation.ui.screen.settings.SettingsViewModel
import com.vastausf.wesolient.presentation.ui.screen.scopeSelect.ScopeSelectScreen
import com.vastausf.wesolient.presentation.ui.screen.scopeSelect.ScopeSelectViewModel
import com.vastausf.wesolient.presentation.ui.screen.settings.SettingsScreen
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

        NavHost(navController, startDestination = "scopeList") {
            composable("scopeList") {
                val scopeSelectViewModel: ScopeSelectViewModel = hiltNavGraphViewModel()

                ScopeSelectScreen(scopeSelectViewModel, navController)
            }

            composable("settings") {
                val settingsViewModel: SettingsViewModel = hiltNavGraphViewModel()

                SettingsScreen(settingsViewModel)
            }

            composable("scope/{scopeUid}") { backStackEntry ->
                val scopeUid = backStackEntry.arguments?.getString("scopeUid")

                val chatViewModel: ChatViewModel = hiltNavGraphViewModel()

                Text("Chat $scopeUid")
            }
        }
    }
}
