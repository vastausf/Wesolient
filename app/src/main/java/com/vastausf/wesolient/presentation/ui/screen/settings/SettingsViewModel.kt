package com.vastausf.wesolient.presentation.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.store.settings.SettingsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val settingsStore: SettingsStore
) : ViewModel() {
    val settingsState = settingsStore.getSettings()

    fun updateSettings(
        block: Settings.() -> Unit
    ) {
        settingsStore.updateSettings(block)
    }
}
