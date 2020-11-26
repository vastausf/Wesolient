package com.vastausf.wesolient.presentation.ui.fragment.settings

import com.google.firebase.auth.FirebaseAuth
import com.vastausf.wesolient.model.ScopeStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsPresenter
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : MvpPresenter<SettingsView>() {
    fun onLogout() {
        firebaseAuth.signOut()
        viewState.logout()
    }
}
