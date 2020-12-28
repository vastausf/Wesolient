package com.vastausf.wesolient.presentation.ui.dialog.createVariable

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
import com.vastausf.wesolient.databinding.DialogCreateVariableBinding
import com.vastausf.wesolient.filterHandled
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateVariableDialog : BottomSheetDialogFragment() {
    private val viewModel: CreateVariableViewModel by viewModels()

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

                viewModel.createNewVariable(title, value)
            }

            etVariableTitle.doAfterTextChanged {
                bCreateVariable.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart(args.scopeUid)

        lifecycleScope.launch {
            viewModel.dialogState
                .collect {
                    if (!it) dialog?.dismiss()
                }
        }

        lifecycleScope.launch {
            viewModel.messageFlow
                .filterHandled()
                .collect {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
        }
    }
}
