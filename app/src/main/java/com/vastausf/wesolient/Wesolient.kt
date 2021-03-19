package com.vastausf.wesolient

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class Wesolient : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}
