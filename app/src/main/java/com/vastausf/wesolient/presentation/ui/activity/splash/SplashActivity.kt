package com.vastausf.wesolient.presentation.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity

@AndroidEntryPoint
class SplashActivity : MvpAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finishAffinity()
    }
}
