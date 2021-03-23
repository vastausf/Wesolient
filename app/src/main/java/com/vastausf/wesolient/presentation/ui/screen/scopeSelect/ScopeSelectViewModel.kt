package com.vastausf.wesolient.presentation.ui.screen.scopeSelect

import androidx.lifecycle.ViewModel
import com.vastausf.wesolient.model.store.scope.ScopeStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScopeSelectViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore
) : ViewModel() {
    val scopeList = scopeStore.getScopeList()

    fun onScopeCreate(
        title: String,
        url: String
    ) {
        scopeStore.createScope(
            title = title,
            url = url
        )
    }
}
