package com.vastausf.wesolient.presentation.ui.dialog.createTemplate

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.TemplateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateTemplateViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value
            })
    }

    fun onNewTemplateCreate(
        title: String,
        message: String
    ) {
        templateStore.createTemplate(scope.uid, title, message,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.create_template_failure)
            }
        )
    }
}
