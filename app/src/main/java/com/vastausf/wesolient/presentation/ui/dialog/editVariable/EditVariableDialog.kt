package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.DialogEditVariableBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class EditVariableDialog : MvpBottomSheetDialogFragment(), EditVariableView {
    @Inject
    lateinit var presenterProvider: Provider<EditVariablePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<EditVariableDialogArgs>()

    private lateinit var binding: DialogEditVariableBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditVariableBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bApply.setOnClickListener {
                val newTitle = etVariableTitle.text.toString().trim()
                val newUrl = etVariableValue.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            etVariableTitle.doAfterTextChanged {
                bApply.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.scopeUid, args.variableUid)
    }

    override fun bindField(title: String, value: String) {
        binding.apply {
            etVariableTitle.setText(title)
            etVariableValue.setText(value)
        }
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }

    override fun onApplyFailure() {
        Toast.makeText(context, R.string.edit_variable_failure, Toast.LENGTH_SHORT).show()
    }
}
