package com.vastausf.wesolient.presentation.ui.dialog.editScope

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditScopePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<EditScopeView>() {
    private lateinit var scope: Scope

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid) { value ->
            scope = value

            viewState.bindField(value.title, value.url)
        }
    }

    fun onApply(newTitle: String, newUrl: String) {
        scopeStore.editScope(scope.uid, newTitle, newUrl)

        viewState.onApplySuccess()
    }
}
