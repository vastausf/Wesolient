package com.vastausf.wesolient.presentation.ui.dialog.createScope

import com.vastausf.wesolient.model.ScopeStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CreateScopeDialogPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<CreateScopeDialogView>() {
    fun onNewScopeCreate(
        title: String,
        url: String
    ) {
        if (scopeStore.getAll().map { it.title }.contains(title)) {
            viewState.showConflict()
        } else {
            scopeStore.create(title, url)

            viewState.dismissDialog()
        }
    }
}
