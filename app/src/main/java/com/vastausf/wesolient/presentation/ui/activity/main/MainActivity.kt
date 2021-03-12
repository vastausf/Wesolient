package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vastausf.wesolient.presentation.design.WesolientTheme
import com.vastausf.wesolient.presentation.ui.fragment.scopeSelect.ScopeList
import com.vastausf.wesolient.presentation.ui.fragment.scopeSelect.ScopeSelectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WesolientTheme {
                AppNavigation()
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "scopeList") {
            composable("scopeList") {
                val scopeSelectViewModel: ScopeSelectViewModel = hiltNavGraphViewModel()

                ScopeList(scopeSelectViewModel)
            }
        }
    }
}
