package com.vastausf.wesolient.presentation.ui.fragment.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.store.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel
@ViewModelInject
constructor(
    private val settingsStore: SettingsStore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _logOut = MutableStateFlow(false)
    val logOut: StateFlow<Boolean> = _logOut

    private val _autoConnectState = MutableStateFlow<Boolean?>(null)
    val autoConnectState: StateFlow<Boolean?> = _autoConnectState

    private val _reconnectCountState = MutableStateFlow<Int?>(null)
    val reconnectCountState: StateFlow<Int?> = _reconnectCountState

    private lateinit var settings: Settings

    fun onStart() {
        settingsStore.getSettingsOnce { settings ->
            this.settings = settings

            _autoConnectState.value = settings.autoConnect
            _reconnectCountState.value = settings.reconnectCount
        }
    }

    fun onAutoConnectUpdate(newValue: Boolean) {
        settings.autoConnect = newValue
    }

    fun onReconnectCountUpdate(newValue: Int) {
        settings.reconnectCount = newValue
    }

    fun saveSettings() {
        settingsStore.editSettings(settings)
    }

    fun logout() {
        firebaseAuth.signOut()
        _logOut.value = true
    }
}
