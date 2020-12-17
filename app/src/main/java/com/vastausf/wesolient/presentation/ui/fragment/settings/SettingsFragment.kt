package com.vastausf.wesolient.presentation.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            bBack.setOnClickListener {
                findNavController().popBackStack()
            }

            bSaveSettings.setOnClickListener {
                viewModel.saveSettings()
                findNavController().popBackStack()
            }

            sAutoConnect.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onAutoConnectUpdate(isChecked)
            }

            etReconnectCount.hint = resources
                .getInteger(R.integer.settings_reconnect_count_default).toString()
            etReconnectCount.doAfterTextChanged { text ->
                viewModel.onReconnectCountUpdate(
                    if (text.isNullOrBlank()) {
                        resources.getInteger(R.integer.settings_reconnect_count_default)
                    } else {
                        text.toString().toInt()
                    }
                )
            }

            bLogOut.setOnClickListener {
                viewModel.logout()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()

        lifecycleScope.launch {
            viewModel.logOut
                .collect {
                    if (it) {
                        findNavController().navigate(R.id.splashActivity)
                        requireActivity().finishAffinity()
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.autoConnectState
                .filterNotNull()
                .collect {
                    binding.sAutoConnect.isChecked = it
                }
        }

        lifecycleScope.launch {
            viewModel.reconnectCountState
                .filterNotNull()
                .collect {
                    binding.etReconnectCount.setText(it.toString())
                }
        }
    }
}
