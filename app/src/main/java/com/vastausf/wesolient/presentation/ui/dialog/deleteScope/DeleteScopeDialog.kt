package com.vastausf.wesolient.presentation.ui.dialog.deleteScope

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_delete_scope.*
import moxy.MvpAppCompatDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class DeleteScopeDialog : MvpAppCompatDialogFragment(), DeleteScopeDialogView {
    @Inject
    lateinit var presenterProvider: Provider<DeleteScopeDialogPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<DeleteScopeDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog
            .Builder(requireContext())

        dialog.setView(R.layout.dialog_delete_scope)

        return dialog.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStart() {
        super.onStart()

        requireDialog().apply {
            deleteB.setOnClickListener {
                presenter.onDelete(args.uid)
            }

            cancelB.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDelete() {
        Toast.makeText(context, R.string.deleted_success, Toast.LENGTH_SHORT).show()

        findNavController()
            .popBackStack()
    }

    override fun onFailure() {
        Toast.makeText(context, R.string.deleted_failure, Toast.LENGTH_SHORT).show()

        findNavController()
            .popBackStack()
    }
}
