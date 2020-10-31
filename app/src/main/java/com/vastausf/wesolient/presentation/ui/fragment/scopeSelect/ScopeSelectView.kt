package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface ScopeSelectView: MvpView {
    @AddToEndSingle
    fun updateScopeList(scopeList: List<String>)

    @Skip
    fun showCreateDialog()

    @AddToEndSingle
    fun updateLoadState(newState: Boolean)
}
