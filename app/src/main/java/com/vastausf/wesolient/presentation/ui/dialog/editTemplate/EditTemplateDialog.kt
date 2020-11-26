package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_edit_scope.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class EditTemplateDialog : MvpBottomSheetDialogFragment(), EditTemplateView {
    @Inject
    lateinit var presenterProvider: Provider<EditTemplatePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<EditTemplateDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            bApply.setOnClickListener {
                val newTitle = etTitle.text.toString().trim()
                val newUrl = etUrl.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            etTitle.doAfterTextChanged {
                bApply.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.scopeUid, args.templateUid)
    }

    override fun bindField(title: String, message: String) {
        view.apply {
            etTitle.setText(title)
            etUrl.setText(message)
        }
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }

    override fun onApplyFailure() {
        Toast.makeText(context, R.string.template_edit_failure, Toast.LENGTH_SHORT).show()
    }
}
