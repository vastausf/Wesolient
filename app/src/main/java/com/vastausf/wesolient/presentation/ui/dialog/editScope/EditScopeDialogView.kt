package com.vastausf.wesolient.presentation.ui.dialog.editScope

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface EditScopeDialogView : MvpView {
    @AddToEndSingle
    fun bindField(title: String, url: String)

    @AddToEndSingle
    fun onApplySuccess()
}
