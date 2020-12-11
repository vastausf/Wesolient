package com.vastausf.wesolient.presentation.ui.fragment.settings

import com.google.firebase.auth.FirebaseAuth
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.store.SettingsStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter
@Inject
constructor(
    private val settingsStore: SettingsStore,
    private val firebaseAuth: FirebaseAuth
) : MvpPresenter<SettingsView>() {
    private lateinit var settings: Settings

    fun onStart() {
        settingsStore.getSettingsOnce { settings ->
            this.settings = settings

            viewState.bindSetting(settings)
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

    fun onLogout() {
        firebaseAuth.signOut()
        viewState.logout()
    }
}
