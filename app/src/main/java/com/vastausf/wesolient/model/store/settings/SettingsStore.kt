package com.vastausf.wesolient.model.store.settings

import com.vastausf.wesolient.data.common.Settings
import kotlinx.coroutines.flow.StateFlow

interface SettingsStore {
    fun getSettings(): StateFlow<Settings>

    fun updateSettings(block: Settings.() -> Unit)
}
