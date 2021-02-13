package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VariableSelectViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private val _variableList = MutableStateFlow<List<Variable>>(emptyList())
    val variableList: StateFlow<List<Variable>> = _variableList

    lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadVariables()
            }
        )
    }

    private fun loadVariables() {
        variableStore.onVariableListUpdate(scope.uid,
            onUpdate = { variableList ->
                _variableList.value = variableList
            }
        )
    }

    fun delete(uid: String) {
        variableStore.deleteVariable(scope.uid, uid,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.select_variable_delete_failure)
            }
        )
    }
}
