package com.vastausf.wesolient.presentation.ui.dialog.createVariable

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateVariableViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid) { value ->
            scope = value
        }
    }

    fun createNewVariable(
        title: String,
        message: String
    ) {
        variableStore.createVariable(scope.uid, title, message,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.create_variable_failure)
            }
        )
    }
}
