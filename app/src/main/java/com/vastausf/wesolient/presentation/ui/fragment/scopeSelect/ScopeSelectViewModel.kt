package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScopeSelectViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    private val _scopeList = MutableStateFlow<List<Scope>>(emptyList())
    val scopeList: StateFlow<List<Scope>> = _scopeList

    private val _createDialogState = MutableStateFlow<SingleEvent<Boolean>?>(null)
    val createDialogState: StateFlow<SingleEvent<Boolean>?> = _createDialogState

    fun onStart() {
        scopeStore.onScopeListUpdate(
            onUpdate = { scopeList ->
                _scopeList.value = scopeList
            }
        )
    }

    fun showCreateDialog() {
        _createDialogState.value = SingleEvent(true)
    }

    fun deleteScope(uid: String) {
        scopeStore.deleteScope(uid)
    }
}
