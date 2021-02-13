package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScopeSelectViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    private val _scopeList = MutableStateFlow<List<Scope>>(emptyList())
    val scopeList: StateFlow<List<Scope>> = _scopeList

    private val _createDialogState = MutableStateFlow<SingleEvent<Boolean>?>(null)
    val createDialogState: StateFlow<SingleEvent<Boolean>?> = _createDialogState

    fun onCreate() {
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
