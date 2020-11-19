package com.vastausf.wesolient.presentation.ui.fragment.settings

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface SettingsView : MvpView {
    @Skip
    fun logout()
}
