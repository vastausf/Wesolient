package com.vastausf.wesolient.presentation.ui.dialog.editScope

import com.vastausf.wesolient.data.Scope
import com.vastausf.wesolient.model.ScopeStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditScopeDialogPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<EditScopeDialogView>() {
    private lateinit var scope: Scope

    fun provide(id: String) {
        scopeStore.getById(id)?.let {
            scope = it

            viewState.bindField(it.title, it.url)
        } ?: viewState.scopeNotFound()
    }

    fun onApply(newTitle: String, newUrl: String) {
        scopeStore
            .edit(scope.id, newTitle, newUrl)

        viewState.onApplySuccess()
    }
}
