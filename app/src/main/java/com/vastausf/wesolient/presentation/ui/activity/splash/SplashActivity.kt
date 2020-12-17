package com.vastausf.wesolient.presentation.ui.activity.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    val viewModel: SplashActivityViewModel by viewModels()
    private val rcSignIn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beginAuth()
    }

    private fun beginAuth() {
        if (Firebase.auth.currentUser != null) {
            onAuthSuccess()
        } else {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
            )

            startActivityForResult(
                AuthUI
                    .getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.LoginTheme)
                    .setIsSmartLockEnabled(false)
                    .build(),
                rcSignIn
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {
            if (resultCode == Activity.RESULT_OK) {
                onAuthSuccess()
            } else {
                onBackPressed()
            }
        }
    }

    private fun onAuthSuccess() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finishAffinity()
    }
}
