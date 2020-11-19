package com.vastausf.wesolient.presentation.ui.fragment.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SettingsFragment : MvpAppCompatFragment(R.layout.fragment_settings), SettingsView {
    @Inject
    lateinit var presenterProvider: Provider<SettingsPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            logoutB.setOnClickListener {
                presenter.onLogout()
            }
        }
    }

    override fun logout() {
        findNavController()
            .navigate(R.id.splashActivity)
        requireActivity().finishAffinity()
    }
}
