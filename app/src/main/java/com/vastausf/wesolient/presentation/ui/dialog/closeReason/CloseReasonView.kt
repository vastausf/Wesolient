package com.vastausf.wesolient.presentation.ui.dialog.closeReason

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface CloseReasonView : MvpView {
    @Skip
    fun onUsedReservedCode()

    @Skip
    fun sendCloseReason(code: Int, message: String)
}
