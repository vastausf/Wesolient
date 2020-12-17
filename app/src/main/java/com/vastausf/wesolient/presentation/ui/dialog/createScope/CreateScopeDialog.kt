package com.vastausf.wesolient.presentation.ui.dialog.createScope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vastausf.wesolient.databinding.DialogCreateScopeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateScopeDialog : BottomSheetDialogFragment() {
    private val viewModel: CreateScopeViewModel by viewModels()

    private lateinit var binding: DialogCreateScopeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateScopeBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onStart() {
        super.onStart()

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
            viewModel.dialogState
                .collect {
                    if (!it)
                        dialog?.dismiss()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bCreateScope.setOnClickListener {
                val title = etScopeTitle.text.toString().trim()
                val url = etScopeUrl.text.toString().trim()

                viewModel.onNewScopeCreate(title, url)
            }

            etScopeTitle.doAfterTextChanged {
                bCreateScope.isEnabled = it.toString().isNotBlank()
            }
        }
    }
}
