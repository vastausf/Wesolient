package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScopeSelectViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    init {
        scopeStore.onScopeListUpdate(
            onUpdate = { scopeList ->
                _scopeList.value = scopeList
            }
        )
    }

    private val _scopeList = MutableStateFlow(emptyList<Scope>())
    val scopeList = _scopeList.asStateFlow()

    private val _newScopeTitle = MutableStateFlow("")
    var newScopeTitle = _newScopeTitle.asStateFlow()
    fun onNewScopeTitleChange(value: String) {
        _newScopeTitle.value = value
    }

    private val _newScopeUrl = MutableStateFlow("")
    var newScopeUrl = _newScopeUrl.asStateFlow()
    fun onNewScopeUrlChange(value: String) {
        _newScopeUrl.value = value
    }

    fun onScopeCreate(
        title: String,
        url: String
    ) {
        scopeStore.createScope(
            title,
            url,
            onSuccess = {

            },
            onFailure = {

            }
        )
    }

    fun onScopeEdit(
        uid: String,
        title: String,
        url: String
    ) {
        scopeStore.editScope(
            uid,
            title,
            url,
            onSuccess = {

            },
            onFailure = {

            }
        )
    }

    fun deleteScope(uid: String) {
        scopeStore.deleteScope(uid)
    }
}
