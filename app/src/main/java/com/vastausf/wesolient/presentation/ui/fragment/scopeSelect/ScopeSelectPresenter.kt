package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.UpdateListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class ScopeSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<ScopeSelectView>() {
    override fun onFirstViewAttach() {
        scopeStore.onUpdate(object : UpdateListener<List<Scope>> {
            override fun onUpdate(snapshot: List<Scope>) {
                viewState.updateScopeList(snapshot)
            }
        })
    }

    fun onCreateScope() {
        viewState.showCreateDialog()
    }

    fun onDeleteScope(uid: String) {
        scopeStore.deleteScope(uid)
    }
}
