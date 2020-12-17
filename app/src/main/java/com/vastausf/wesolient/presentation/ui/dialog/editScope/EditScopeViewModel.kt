package com.vastausf.wesolient.presentation.ui.dialog.editScope

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditScopeViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private val _titleField = MutableStateFlow("")
    val titleField: StateFlow<String> = _titleField

    private val _urlField = MutableStateFlow("")
    val urlField: StateFlow<String> = _urlField

    private lateinit var scope: Scope

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid) { value ->
            scope = value

            _titleField.value = value.title
            _urlField.value = value.url
        }
    }

    fun apply(newTitle: String, newUrl: String) {
        scopeStore.editScope(scope.uid, newTitle, newUrl)

        _dialogState.value = false
    }
}
