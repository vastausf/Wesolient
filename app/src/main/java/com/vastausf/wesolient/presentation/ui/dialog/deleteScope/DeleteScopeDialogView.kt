package com.vastausf.wesolient.presentation.ui.dialog.deleteScope

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface DeleteScopeDialogView : MvpView {
    @AddToEndSingle
    fun onDelete()

    @AddToEndSingle
    fun onFailure()
}
