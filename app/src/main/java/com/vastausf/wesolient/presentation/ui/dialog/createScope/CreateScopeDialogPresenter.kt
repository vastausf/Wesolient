package com.vastausf.wesolient.presentation.ui.dialog.createScope

import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.WriteListener
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
        scopeStore.createScope(title, url, object : WriteListener {
            override fun onSuccess() {
                viewState.dismissDialog()
            }

            override fun onFailure() {
                viewState.showConflict()
            }
        })
    }
}
