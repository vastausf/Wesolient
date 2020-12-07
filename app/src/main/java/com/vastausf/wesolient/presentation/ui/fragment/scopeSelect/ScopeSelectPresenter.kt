package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.vastausf.wesolient.model.store.ScopeStore
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
        scopeStore.onScopeListUpdate(
            onUpdate = { scopeList ->
                viewState.updateScopeList(scopeList)
            }
        )
    }

    fun onCreateScope() {
        viewState.showCreateDialog()
    }

    fun onDeleteScope(uid: String) {
        scopeStore.deleteScope(uid)
    }
}
