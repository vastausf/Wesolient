package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface ScopeSelectView: MvpView {
    @AddToEndSingle
    fun updateScopeList(scopeList: List<String>)

    @AddToEndSingle
    fun showCreateDialog()

    @AddToEndSingle
    fun updateLoadState(newState: Boolean)
}
