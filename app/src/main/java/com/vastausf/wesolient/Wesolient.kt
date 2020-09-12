package com.vastausf.wesolient

import android.app.Application

class Wesolient: Application() {
    companion object {
        lateinit var instance: Wesolient
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}