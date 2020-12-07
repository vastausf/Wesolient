package com.vastausf.wesolient.presentation.ui.dialog.createTemplate

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.TemplateStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CreateTemplatePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : MvpPresenter<CreateTemplateView>() {
    private lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value
            })
    }

    fun onNewTemplateCreate(
        title: String,
        message: String
    ) {
        templateStore.createTemplate(scope.uid, title, message,
            onSuccess = {
                viewState.dismissDialog()
            },
            onFailure = {
                viewState.showErrorMessage()
            }
        )
    }
}
