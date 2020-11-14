package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.UpdateListener
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ScopeSelectPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<ScopeSelectFragmentView>() {

    override fun onFirstViewAttach() {
        updateScopeList()
    }

    private fun updateScopeList() {
        presenterScope.launch {
            scopeStore.onUpdate(object : UpdateListener<List<Scope>> {
                override fun onUpdate(snapshot: List<Scope>) {
                    viewState.updateLoadState(true)

                    viewState.updateScopeList(snapshot)

                    viewState.updateLoadState(false)
                }
            })
        }
    }

    fun onRefresh() {
        updateScopeList()
    }

    fun onClickCreateNew() {
        viewState.showCreateDialog()
    }
}
