package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.google.firebase.analytics.FirebaseAnalytics
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.ChangeListener
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ScopeSelectPresenter
@Inject
constructor(
    private val scopeStoreRealm: ScopeStore,
    private val firebaseAnalytics: FirebaseAnalytics
) : MvpPresenter<ScopeSelectView>() {
    private val scopeChangeListener = ChangeListener {
        updateScopeList()
    }

    override fun onFirstViewAttach() {
        scopeStoreRealm.registerListener(scopeChangeListener)
        updateScopeList()
    }

    private fun updateScopeList() {
        viewState.updateLoadState(true)

        firebaseAnalytics.logEvent("scope_opening", null)

        presenterScope.launch {
            viewState.updateScopeList(scopeStoreRealm.getAll())

            viewState.updateLoadState(false)
        }
    }

    fun onRefresh() {
        updateScopeList()
    }

    fun onClickCreateNew() {
        viewState.showCreateDialog()
    }

    override fun onDestroy() {
        scopeStoreRealm.unregisterListener(scopeChangeListener)
    }
}
