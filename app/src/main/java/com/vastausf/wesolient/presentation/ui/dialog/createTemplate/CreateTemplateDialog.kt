package com.vastausf.wesolient.presentation.ui.dialog.createTemplate

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
import com.vastausf.wesolient.databinding.DialogCreateTemplateBinding
import com.vastausf.wesolient.filterHandled
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateTemplateDialog : BottomSheetDialogFragment() {
    private val viewModel: CreateTemplateViewModel by viewModels()
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

    override fun onStart() {
        super.onStart()

        viewModel.onStart(args.scopeUid)

        lifecycleScope.launch {
            viewModel.messageFlow
                .filterHandled()
                .collect {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launch {
            viewModel.dialogState
                .collect {
                    if (!it)
                        dialog?.dismiss()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bCreateTemplate.setOnClickListener {
                val title = etTemplateTitle.text.toString().trim()
                val message = etTemplateMessage.text.toString().trim()

                viewModel.onNewTemplateCreate(title, message)
            }

            etTemplateTitle.doAfterTextChanged {
                bCreateTemplate.isEnabled = it.toString().isNotBlank()
            }
        }
    }
}
