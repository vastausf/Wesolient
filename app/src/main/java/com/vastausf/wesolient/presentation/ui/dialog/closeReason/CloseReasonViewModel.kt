package com.vastausf.wesolient.presentation.ui.dialog.closeReason

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.client.CloseReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CloseReasonViewModel
@ViewModelInject
constructor(

) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _closeReasonFlow = MutableStateFlow<CloseReason?>(null)
    val closeReasonFlow: StateFlow<CloseReason?> = _closeReasonFlow

    fun onDisconnect(code: Int, message: String) {
        if ((code in 1004..1006) || (code in 1012..2999)) {
            _messageFlow.value = SingleEvent(R.string.close_reason_used_reserved_code)
        } else {
            _closeReasonFlow.value = CloseReason(code, message)
        }
    }
}
