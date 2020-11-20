package com.vastausf.wesolient.presentation.ui.dialog.closeReason

import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CloseReasonPresenter
@Inject
constructor(

) : MvpPresenter<CloseReasonView>() {
    fun onDisconnect(code: Int, message: String) {
        if ((code in 1004..1006) || (code in 1012..2999)) {
            viewState.onUsedReservedCode()
        } else {
            viewState.sendCloseReason(code, message)
        }
    }
}
