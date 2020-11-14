package com.vastausf.wesolient.presentation.ui.fragment.settings

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface SettingsFragmentView : MvpView {
    @AddToEndSingle
    fun signOut()
}
