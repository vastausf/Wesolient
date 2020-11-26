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
import com.vastausf.wesolient.databinding.DialogEditTemplateBinding
import dagger.hilt.android.AndroidEntryPoint
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

    private lateinit var binding: DialogEditTemplateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditTemplateBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bApply.setOnClickListener {
                val newTitle = etTemplateTitle.text.toString().trim()
                val newUrl = etTemplateMessage.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            etTemplateTitle.doAfterTextChanged {
                bApply.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.scopeUid, args.templateUid)
    }

    override fun bindField(title: String, message: String) {
        binding.apply {
            etTemplateTitle.setText(title)
            etTemplateMessage.setText(message)
        }
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }

    override fun onApplyFailure() {
        Toast.makeText(context, R.string.edit_template_failure, Toast.LENGTH_SHORT).show()
    }
}
