package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.CreateListener
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditVariablePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<EditVariableView>() {
    private lateinit var scope: Scope
    private lateinit var variable: Variable

    fun onStart(scopeUid: String, variableUid: String) {
        scopeStore.getScopeOnce(scopeUid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                loadVariable(variableUid)
            }
        })
    }

    fun loadVariable(uid: String) {
        scopeStore.getVariableOnce(scope.uid, uid, object : ValueListener<Variable> {
            override fun onSuccess(value: Variable) {
                variable = value

                viewState.bindField(value.title, value.value)
            }
        })
    }

    fun onApply(newTitle: String, newValue: String) {
        scopeStore.editVariable(
            scope.uid,
            variable.uid,
            newTitle,
            newValue,
            object : CreateListener {
                override fun onSuccess() {
                    viewState.onApplySuccess()
                }

                override fun onFailure() {
                    viewState.onApplyFailure()
                }
            })

        viewState.onApplySuccess()
    }
}
