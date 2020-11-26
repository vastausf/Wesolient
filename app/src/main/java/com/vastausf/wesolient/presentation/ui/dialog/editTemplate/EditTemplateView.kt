package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface EditTemplateView : MvpView {
    @AddToEndSingle
    fun bindField(title: String, message: String)

    @AddToEndSingle
    fun onApplySuccess()

    @AddToEndSingle
    fun onApplyFailure()
}
