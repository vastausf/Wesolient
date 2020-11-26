package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

import com.vastausf.wesolient.data.common.Variable
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface VariableSelectView : MvpView {
    @AddToEndSingle
    fun bindVariableList(variableList: List<Variable>)

    @Skip
    fun onDeleteSuccess()

    @Skip
    fun onDeleteFailure()
}
