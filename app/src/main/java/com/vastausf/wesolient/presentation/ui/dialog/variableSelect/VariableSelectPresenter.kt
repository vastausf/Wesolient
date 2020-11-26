package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.DeleteListener
import com.vastausf.wesolient.model.listener.UpdateListener
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class VariableSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<VariableSelectView>() {
    lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                loadVariables()
            }
        })
    }

    private fun loadVariables() {
        scopeStore.onVariableListUpdate(scope.uid, object : UpdateListener<List<Variable>> {
            override fun onUpdate(snapshot: List<Variable>) {
                viewState.bindVariableList(snapshot)
            }
        })
    }

    fun onDelete(uid: String) {
        scopeStore.deleteVariable(scope.uid, uid, object : DeleteListener {
            override fun onSuccess() {
                viewState.onDeleteSuccess()
            }

            override fun onFailure() {
                viewState.onDeleteFailure()
            }
        })
    }
}
