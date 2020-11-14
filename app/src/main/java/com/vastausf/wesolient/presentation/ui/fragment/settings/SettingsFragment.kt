package com.vastausf.wesolient.presentation.ui.fragment.settings

import android.content.Intent
import android.os.Bundle
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.activity.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SettingsFragment : MvpAppCompatFragment(R.layout.fragment_settings), SettingsFragmentView {
    @Inject
    lateinit var presenterProvider: Provider<SettingsFragmentPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            logoutB.setOnClickListener {
                presenter.onLogOut()
            }
        }
    }

    override fun signOut() {
        startActivity(Intent(context, SplashActivity::class.java))
        activity?.finishAffinity()
    }
}
