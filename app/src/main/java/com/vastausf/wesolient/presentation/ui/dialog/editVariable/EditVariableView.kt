package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface EditVariableView : MvpView {
    @AddToEndSingle
    fun bindField(title: String, value: String)

    @AddToEndSingle
    fun onApplySuccess()

    @AddToEndSingle
    fun onApplyFailure()
}
