package com.vastausf.wesolient.presentation.ui.dialog.createTemplate

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface CreateTemplateView : MvpView {
    @Skip
    fun dismissDialog()

    @Skip
    fun showErrorMessage()
}
