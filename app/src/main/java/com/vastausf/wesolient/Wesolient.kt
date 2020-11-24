package com.vastausf.wesolient

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Wesolient : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.database.setPersistenceEnabled(true)
        AndroidThreeTen.init(this)
    }
}
