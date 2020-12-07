package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.TemplateStore
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditTemplatePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore
) : MvpPresenter<EditTemplateView>() {
    private lateinit var scope: Scope
    private lateinit var template: Template

    fun onStart(scopeUid: String, templateUid: String) {
        scopeStore.getScopeOnce(scopeUid,
            onSuccess = { value ->
                scope = value

                loadTemplate(templateUid)
            }
        )
    }

    private fun loadTemplate(uid: String) {
        templateStore.getTemplateOnce(scope.uid, uid,
            onSuccess = { value ->
                template = value

                viewState.bindField(value.title, value.message)
            }
        )
    }

    fun onApply(newTitle: String, newMessage: String) {
        templateStore.editTemplate(
            scope.uid,
            template.uid,
            newTitle,
            newMessage,
            onSuccess = {
                viewState.onApplySuccess()
            },
            onFailure = {
                viewState.onApplyFailure()
            }
        )

        viewState.onApplySuccess()
    }
}
