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

    fun onScopeCreate(
        title: String,
        url: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        scopeStore.createScope(
            title = title,
            url = url,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun onScopeEdit(
        uid: String,
        title: String,
        url: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        scopeStore.editScope(
            uid = uid,
            newTitle = title,
            newUrl = url,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun deleteScope(uid: String) {
        scopeStore.deleteScope(uid)
    }
}
