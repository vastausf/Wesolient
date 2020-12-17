package com.vastausf.wesolient.presentation.ui.dialog.editVariable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vastausf.wesolient.databinding.DialogEditVariableBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditVariableDialog : BottomSheetDialogFragment() {
    private val viewModel: EditVariableViewModel by viewModels()

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

                viewModel.apply(
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

        viewModel.onStart(args.scopeUid, args.variableUid)

        lifecycleScope.launch {
            viewModel.dialogState
                .collect {
                    if (!it) dialog?.dismiss()
                }
        }

        lifecycleScope.launch {
            viewModel.messageFlow.filterNotNull()
                .map {
                    it.getValueIfNotHandled()
                }.filterNotNull()
                .collect {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launch {
            viewModel.titleField
                .collect {
                    binding.etVariableTitle.setText(it)
                }
        }

        lifecycleScope.launch {
            viewModel.valueField
                .collect {
                    binding.etVariableValue.setText(it)
                }
        }
    }
}
