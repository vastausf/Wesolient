package com.vastausf.wesolient.presentation.ui.dialog.editTemplate

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
import com.vastausf.wesolient.databinding.DialogEditTemplateBinding
import com.vastausf.wesolient.filterHandled
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditTemplateDialog : BottomSheetDialogFragment() {
    private val viewModel: EditTemplateViewModel by viewModels()

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

                viewModel.apply(
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

        viewModel.onStart(args.scopeUid, args.templateUid)

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

        lifecycleScope.launch {
            viewModel.titleField
                .collect {
                    binding.etTemplateTitle.setText(it)
                }
        }

        lifecycleScope.launch {
            viewModel.messageField
                .collect {
                    binding.etTemplateMessage.setText(it)
                }
        }
    }
}
