package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import com.vastausf.wesolient.data.common.Scope
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface ScopeSelectView : MvpView {
    @AddToEndSingle
    fun updateScopeList(scopeList: List<Scope>)

    @Skip
    fun showCreateDialog()
}
