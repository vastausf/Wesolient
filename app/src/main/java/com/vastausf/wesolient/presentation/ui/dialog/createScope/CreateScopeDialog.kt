package com.vastausf.wesolient.presentation.ui.dialog.createScope

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CreateScopeDialog : MvpAppCompatDialogFragment(), CreateScopeDialogView {
    @Inject
    lateinit var presenterProvider: Provider<CreateScopeDialogPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog
            .Builder(requireContext())

        dialog.setView(R.layout.dialog_create_scope)

        return dialog.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
