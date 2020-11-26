package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

import com.vastausf.wesolient.data.common.Template
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface TemplateSelectView : MvpView {
    @AddToEndSingle
    fun bindTemplateList(templateList: List<Template>)

    @AddToEndSingle
    fun onError()

    @Skip
    fun onDeleteSuccess()

    @Skip
    fun onDeleteFailure()
}
