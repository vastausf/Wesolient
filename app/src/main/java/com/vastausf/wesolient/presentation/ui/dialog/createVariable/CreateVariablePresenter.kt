package com.vastausf.wesolient.presentation.ui.dialog.createVariable

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.CreateListener
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CreateVariablePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<CreateVariableView>() {
    private lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value
            }
        })
    }

    fun onNewVariableCreate(
        title: String,
        message: String
    ) {
        scopeStore.createVariable(scope.uid, title, message, object : CreateListener {
            override fun onSuccess() {
                viewState.dismissDialog()
            }

            override fun onFailure() {
                viewState.showErrorMessage()
            }
        })
    }
}
