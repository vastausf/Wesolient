package com.vastausf.wesolient.presentation.ui.dialog.createScope

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface CreateScopeDialogView : MvpView {
    @Skip
    fun showConflict()

    @Skip
    fun dismissDialog()
}
