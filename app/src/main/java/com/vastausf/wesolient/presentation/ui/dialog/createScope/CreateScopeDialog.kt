package com.vastausf.wesolient.presentation.ui.dialog.createScope

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_create_scope.*
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CreateScopeDialog : MvpAppCompatDialogFragment(), CreateScopeView {
    @Inject
    lateinit var presenterProvider: Provider<CreateScopePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog
            .Builder(requireContext())

        dialog.setView(R.layout.dialog_create_scope)

        return dialog.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStart() {
        super.onStart()

        requireDialog().apply {
            createB.setOnClickListener {
                val title = newScopeTitleET.text.toString().trim()
                val url = newScopeUrlET.text.toString().trim()

                presenter.onNewScopeCreate(
                    title,
                    url
                )
            }

            newScopeTitleET.doAfterTextChanged {
                createB.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun showErrorMessage() {
        Toast.makeText(context, R.string.scope_create_failure, Toast.LENGTH_SHORT).show()
    }
}
