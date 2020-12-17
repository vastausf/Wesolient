package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditVariableViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private val _titleField = MutableStateFlow("")
    val titleField: StateFlow<String> = _titleField

    private val _valueField = MutableStateFlow("")
    val valueField: StateFlow<String> = _valueField

    private lateinit var scope: Scope
    private lateinit var variable: Variable

    fun onStart(scopeUid: String, variableUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadVariable(variableUid)
            }
        )
    }

    private fun loadVariable(uid: String) {
        variableStore.getVariableOnce(scope.uid, uid,
            onSuccess = { value ->
                variable = value

                _titleField.value = value.title
                _valueField.value = value.value
            }
        )
    }

    fun apply(newTitle: String, newValue: String) {
        variableStore.editVariable(
            scope.uid,
            variable.uid,
            newTitle,
            newValue,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.edit_variable_failure)
            }
        )
    }
}
