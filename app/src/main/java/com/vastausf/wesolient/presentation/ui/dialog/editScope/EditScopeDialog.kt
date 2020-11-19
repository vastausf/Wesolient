package com.vastausf.wesolient.presentation.ui.dialog.editScope

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.dialog.deleteScope.DeleteScopeDialogArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_edit_scope.*
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class EditScopeDialog : MvpAppCompatDialogFragment(), EditScopeDialogView {
    @Inject
    lateinit var presenterProvider: Provider<EditScopePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<DeleteScopeDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog
            .Builder(requireContext())

        dialog.setView(R.layout.dialog_edit_scope)

        return dialog.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.uid)

        requireDialog().apply {
            applyB.setOnClickListener {
                val newTitle = editScopeTitleET.text.toString().trim()
                val newUrl = editScopeUrlET.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            editScopeTitleET.doAfterTextChanged {
                applyB.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun bindField(title: String, url: String) {
        requireDialog().apply {
            editScopeTitleET.setText(title)
            editScopeUrlET.setText(url)
        }
    }

    override fun scopeNotFound() {
        Toast.makeText(context, R.string.miss_scope, Toast.LENGTH_SHORT).show()

        findNavController().popBackStack()
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }
}
