package com.vastausf.wesolient.presentation.ui.fragment.settings

import com.google.firebase.auth.FirebaseAuth
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.UpdateListener
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SettingsPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val firebaseAuth: FirebaseAuth
) : MvpPresenter<SettingsView>() {
    fun onLogout() {
        firebaseAuth.signOut()
        viewState.logout()
    }
}
