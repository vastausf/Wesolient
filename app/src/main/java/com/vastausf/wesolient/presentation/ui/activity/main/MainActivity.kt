package com.vastausf.wesolient.presentation.ui.activity.main

import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainActivityView {
    @Inject
    lateinit var presenterProvider: Provider<MainActivityPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }
}
