package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

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

class TemplateSelectViewModel
@ViewModelInject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : ViewModel() {
    private val _messageFlow = MutableStateFlow<SingleEvent<Int>?>(null)
    val messageFlow: StateFlow<SingleEvent<Int>?> = _messageFlow

    private val _dialogState = MutableStateFlow(true)
    val dialogState: StateFlow<Boolean> = _dialogState

    private val _templateList = MutableStateFlow<List<Template>>(emptyList())
    val templateList: StateFlow<List<Template>> = _templateList

    lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadTemplates()
            }
        )
    }

    private fun loadTemplates() {
        templateStore.onTemplateListUpdate(scope.uid,
            onUpdate = { templateList ->
                _templateList.value = templateList
            }
        )
    }

    fun delete(uid: String) {
        templateStore.deleteTemplate(scope.uid, uid,
            onSuccess = {
                _dialogState.value = false
            },
            onFailure = {
                _messageFlow.value = SingleEvent(R.string.select_template_delete_failure)
            }
        )
    }
}
