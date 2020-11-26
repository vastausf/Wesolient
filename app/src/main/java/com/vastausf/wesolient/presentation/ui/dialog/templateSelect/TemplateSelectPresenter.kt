package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.DeleteListener
import com.vastausf.wesolient.model.listener.UpdateListener
import com.vastausf.wesolient.model.listener.ValueListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class TemplateSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<TemplateSelectView>() {
    lateinit var scope: Scope

    fun onStart(scopeUid: String) {
        scopeStore.getScopeOnce(scopeUid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                loadTemplates()
            }
        })
    }

    private fun loadTemplates() {
        scopeStore.onTemplateListUpdate(scope.uid, object : UpdateListener<List<Template>> {
            override fun onUpdate(snapshot: List<Template>) {
                viewState.bindTemplateList(snapshot)
            }
        })
    }

    fun onDelete(uid: String) {
        scopeStore.deleteTemplate(scope.uid, uid, object : DeleteListener {
            override fun onSuccess() {
                viewState.onDeleteSuccess()
            }

            override fun onFailure() {
                viewState.onDeleteFailure()
            }
        })
    }
}
