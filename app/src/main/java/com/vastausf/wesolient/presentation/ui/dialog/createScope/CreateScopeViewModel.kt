package com.vastausf.wesolient.presentation.ui.dialog.createScope

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.model.store.ScopeStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateScopeViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    fun onNewScopeCreate(
        title: String,
        url: String
    ) {
        scopeStore.createScope(title, url,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.create_scope_failure)
            }
        )
    }
}

