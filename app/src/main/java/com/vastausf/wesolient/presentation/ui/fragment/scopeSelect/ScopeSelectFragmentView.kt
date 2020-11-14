package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.vastausf.wesolient.model.data.Scope
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface ScopeSelectFragmentView : MvpView {
    @AddToEndSingle
    fun updateScopeList(scopeList: List<Scope>)

    @Skip
    fun showCreateDialog()

    @AddToEndSingle
    fun updateLoadState(newState: Boolean)
}
