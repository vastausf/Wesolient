package com.vastausf.wesolient.presentation.ui.fragment.settings

import com.google.firebase.auth.FirebaseAuth
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SettingsFragmentPresenter
@Inject
constructor(
    private val firebaseAuth: FirebaseAuth
) : MvpPresenter<SettingsFragmentView>() {
    fun onLogOut() {
        firebaseAuth.signOut()
        viewState.signOut()
    }
}
