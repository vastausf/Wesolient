package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class VariableSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : MvpPresenter<VariableSelectView>() {
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
                viewState.bindVariableList(variableList)
            }
        )
    }

    fun onDelete(uid: String) {
        variableStore.deleteVariable(scope.uid, uid,
            onSuccess = {
                viewState.onDeleteSuccess()
            },
            onFailure = {
                viewState.onDeleteFailure()
            }
        )
    }
}
