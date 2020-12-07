package com.vastausf.wesolient.presentation.ui.fragment.settings

import com.vastausf.wesolient.data.common.Settings
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface SettingsView : MvpView {
    @Skip
    fun logout()

    @AddToEndSingle
    fun bindSetting(settings: Settings)
}
