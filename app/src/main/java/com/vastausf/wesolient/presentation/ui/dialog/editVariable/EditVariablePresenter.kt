package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.VariableStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditVariablePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val variableStore: VariableStore
) : MvpPresenter<EditVariableView>() {
    private lateinit var scope: Scope
    private lateinit var variable: Variable

    fun onStart(scopeUid: String, variableUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadVariable(variableUid)
            }
        )
    }

    private fun loadVariable(uid: String) {
        variableStore.getVariableOnce(scope.uid, uid,
            onSuccess = { value ->
                variable = value

                viewState.bindField(value.title, value.value)
            }
        )
    }

    fun onApply(newTitle: String, newValue: String) {
        variableStore.editVariable(
            scope.uid,
            variable.uid,
            newTitle,
            newValue,
            onSuccess = {
                viewState.onApplySuccess()
            },
            onFailure = {
                viewState.onApplyFailure()
            }
        )

        viewState.onApplySuccess()
    }
}
