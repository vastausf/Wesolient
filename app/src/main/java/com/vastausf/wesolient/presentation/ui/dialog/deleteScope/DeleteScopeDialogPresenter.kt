package com.vastausf.wesolient.presentation.ui.dialog.deleteScope

import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.listener.DeleteListener
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class DeleteScopeDialogPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore
) : MvpPresenter<DeleteScopeDialogView>() {
    fun onDelete(uid: String) {
        scopeStore
            .deleteScope(uid, object : DeleteListener {
                override fun onSuccess() {
                    viewState.onDelete()
                }

                override fun onFailure() {
                    viewState.onFailure()
                }
            })
    }
}
