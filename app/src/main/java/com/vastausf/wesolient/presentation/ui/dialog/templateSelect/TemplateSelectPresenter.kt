package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.TemplateStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class TemplateSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : MvpPresenter<TemplateSelectView>() {
    lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadTemplates()
            }
        )
    }

    private fun loadTemplates() {
        templateStore.onTemplateListUpdate(scope.uid,
            onUpdate = { templateList ->
                viewState.bindTemplateList(templateList)
            }
        )
    }

    fun onDelete(uid: String) {
        templateStore.deleteTemplate(scope.uid, uid,
            onSuccess = {
                viewState.onDeleteSuccess()
            },
            onFailure = {
                viewState.onDeleteFailure()
            }
        )
    }
}
