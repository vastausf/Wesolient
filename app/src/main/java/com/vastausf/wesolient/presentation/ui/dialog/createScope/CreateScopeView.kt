package com.vastausf.wesolient.presentation.ui.dialog.createScope

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface CreateScopeView : MvpView {
    @Skip
    fun dismissDialog()

    @Skip
    fun showErrorMessage()
}
