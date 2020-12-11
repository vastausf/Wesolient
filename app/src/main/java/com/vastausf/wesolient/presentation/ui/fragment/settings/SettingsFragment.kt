package com.vastausf.wesolient.presentation.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SettingsFragment : MvpAppCompatFragment(R.layout.fragment_settings), SettingsView {
    @Inject
    lateinit var presenterProvider: Provider<SettingsPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            sAutoConnect.setOnCheckedChangeListener { _, isChecked ->
                presenter.onAutoConnectUpdate(isChecked)
            }

            etReconnectCount.hint = resources
                .getInteger(R.integer.settings_reconnect_count_default).toString()
            etReconnectCount.doAfterTextChanged { text ->
                presenter.onReconnectCountUpdate(
                    if (text.isNullOrBlank()) {
                        resources.getInteger(R.integer.settings_reconnect_count_default)
                    } else {
                        text.toString().toInt()
                    }
                )
            }

            bLogOut.setOnClickListener {
                presenter.onLogout()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart()
    }

    override fun bindSetting(settings: Settings) {
        binding.apply {
            sAutoConnect.isChecked = settings.autoConnect
            etReconnectCount.setText(settings.reconnectCount.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.saveSettings()
    }

    override fun logout() {
        findNavController()
            .navigate(R.id.splashActivity)
        requireActivity().finishAffinity()
    }
}
