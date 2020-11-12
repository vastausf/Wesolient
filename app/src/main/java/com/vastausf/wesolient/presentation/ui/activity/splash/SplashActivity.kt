package com.vastausf.wesolient.presentation.ui.activity.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity

@AndroidEntryPoint
class SplashActivity : MvpAppCompatActivity() {
    private val rcSignIn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    .setLogo(R.drawable.ic_app)
                    .build(),
                rcSignIn
            )
        }
    }

    private fun onAuthSuccess() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                onAuthSuccess()
            }
        }
    }
}
