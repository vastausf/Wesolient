package com.vastausf.wesolient.presentation.ui.dialog.editScope

import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditScopePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<EditScopeDialogView>() {
    private lateinit var scope: Scope

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                viewState.bindField(value.title, value.url)
            }
        })
    }

    fun onApply(newTitle: String, newUrl: String) {
        scopeStore
            .editScope(scope.uid, newTitle, newUrl)

        viewState.onApplySuccess()
    }
}
