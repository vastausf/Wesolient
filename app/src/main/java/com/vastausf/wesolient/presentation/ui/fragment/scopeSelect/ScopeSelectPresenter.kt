package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class ScopeSelectPresenter
@Inject
constructor(

) : MvpPresenter<ScopeSelectView>() {
    override fun onFirstViewAttach() {
        viewState.updateScopeList(
            listOf(
                "1'st scope",
                "2'nd scope",
                "3'rd scope",
                "4'th scope",
                "5'th scope",
                "6'th scope",
                "7'th scope",
                "8'th scope",
                "9'th scope",
                "10'th scope"
            )
        )
    }

    fun onClickCreateNew() {
        viewState.showCreateDialog()
    }
}
