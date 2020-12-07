package com.vastausf.wesolient.presentation.ui.dialog.createVariable

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CreateVariablePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : MvpPresenter<CreateVariableView>() {
    private lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid) { value ->
            scope = value
        }
    }

    fun onNewVariableCreate(
        title: String,
        message: String
    ) {
        variableStore.createVariable(scope.uid, title, message,
            onSuccess = {
                viewState.dismissDialog()
            },
            onFailure = {
                viewState.showErrorMessage()
            }
        )
    }
}
