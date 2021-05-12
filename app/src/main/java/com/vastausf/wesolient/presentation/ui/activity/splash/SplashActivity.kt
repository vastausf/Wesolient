package com.vastausf.wesolient.presentation.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalMaterialApi
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finishAffinity()
    }
}
