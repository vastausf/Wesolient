package com.vastausf.wesolient.presentation.ui.dialog.createVariable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.DialogCreateVariableBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CreateVariableDialog : MvpBottomSheetDialogFragment(), CreateVariableView {
    @Inject
    lateinit var presenterProvider: Provider<CreateVariablePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<CreateVariableDialogArgs>()

    private lateinit var binding: DialogCreateVariableBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateVariableBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bCreateVariable.setOnClickListener {
                val title = etVariableTitle.text.toString().trim()
                val value = etVariableValue.text.toString().trim()

                presenter.onNewVariableCreate(title, value)
            }

            etVariableTitle.doAfterTextChanged {
                bCreateVariable.isEnabled = it.toString().isNotBlank()
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
        Toast.makeText(context, R.string.create_variable_failure, Toast.LENGTH_SHORT).show()
    }
}
