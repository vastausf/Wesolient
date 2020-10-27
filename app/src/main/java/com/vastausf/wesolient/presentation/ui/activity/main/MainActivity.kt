package com.vastausf.wesolient.presentation.ui.activity.main

import android.os.Bundle
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.fragment.scopeSelect.ScopeSelectFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, ScopeSelectFragment())
                .commit()
        }
    }
}
