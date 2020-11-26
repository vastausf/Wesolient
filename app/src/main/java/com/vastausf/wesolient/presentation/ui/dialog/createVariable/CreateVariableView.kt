package com.vastausf.wesolient.presentation.ui.dialog.createVariable

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface CreateVariableView : MvpView {
    @Skip
    fun dismissDialog()

    @Skip
    fun showErrorMessage()
}
