package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.CreateListener
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class EditTemplatePresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<EditTemplateView>() {
    private lateinit var scope: Scope
    private lateinit var template: Template

    fun onStart(scopeUid: String, templateUid: String) {
        scopeStore.getScopeOnce(scopeUid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                loadTemplate(templateUid)
            }
        })
    }

    fun loadTemplate(uid: String) {
        scopeStore.getTemplateOnce(scope.uid, uid, object : ValueListener<Template> {
            override fun onSuccess(value: Template) {
                template = value

                viewState.bindField(value.title, value.message)
            }
        })
    }

    fun onApply(newTitle: String, newMessage: String) {
        scopeStore.editTemplate(
            scope.uid,
            template.uid,
            newTitle,
            newMessage,
            object : CreateListener {
                override fun onSuccess() {
                    viewState.onApplySuccess()
                }

                override fun onFailure() {
                    viewState.onApplyFailure()
                }
            })

        viewState.onApplySuccess()
    }
}
