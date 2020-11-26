package com.vastausf.wesolient.presentation.ui.dialog.createTemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.DialogCreateTemplateBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CreateTemplateDialog : MvpBottomSheetDialogFragment(), CreateTemplateView {
    @Inject
    lateinit var presenterProvider: Provider<CreateTemplatePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<CreateTemplateDialogArgs>()

    private lateinit var binding: DialogCreateTemplateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateTemplateBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bCreateTemplate.setOnClickListener {
                val title = etTemplateTitle.text.toString().trim()
                val message = etTemplateMessage.text.toString().trim()

                presenter.onNewTemplateCreate(title, message)
            }

            etTemplateTitle.doAfterTextChanged {
                bCreateTemplate.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.scopeUid)
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun showErrorMessage() {
        Toast.makeText(context, R.string.create_template_failure, Toast.LENGTH_SHORT).show()
    }
}
