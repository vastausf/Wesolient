package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.R
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.TemplateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditTemplateViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private val _titleField = MutableStateFlow("")
    val titleField: StateFlow<String> = _titleField

    private val _messageField = MutableStateFlow("")
    val messageField: StateFlow<String> = _messageField

    private lateinit var scope: Scope
    private lateinit var template: Template

    fun onStart(scopeUid: String, templateUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadTemplate(templateUid)
            }
        )
    }

    private fun loadTemplate(uid: String) {
        templateStore.getTemplateOnce(scope.uid, uid,
            onSuccess = { value ->
                template = value

                _titleField.value = value.title
                _messageField.value = value.message
            }
        )
    }

    fun apply(newTitle: String, newMessage: String) {
        templateStore.editTemplate(
            scope.uid,
            template.uid,
            newTitle,
            newMessage,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.edit_template_failure)
            }
        )
    }
}
