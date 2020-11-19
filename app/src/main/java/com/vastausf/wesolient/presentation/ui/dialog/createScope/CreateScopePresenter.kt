package com.vastausf.wesolient.presentation.ui.dialog.createScope

import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.CreateListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CreateScopePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<CreateScopeView>() {
    fun onNewScopeCreate(
        title: String,
        url: String
    ) {
        scopeStore.createScope(title, url, object : CreateListener {
            override fun onSuccess() {
                viewState.dismissDialog()
            }

            override fun onFailure() {
                viewState.showErrorMessage()
            }
        })
    }
}
