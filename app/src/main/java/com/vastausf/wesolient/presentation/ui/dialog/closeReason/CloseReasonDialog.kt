package com.vastausf.wesolient.presentation.ui.dialog.closeReason

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vastausf.wesolient.data.client.CloseReason
import com.vastausf.wesolient.databinding.DialogCloseReasonBinding
import com.vastausf.wesolient.filterHandled
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.sendDialogResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CloseReasonDialog : BottomSheetDialogFragment() {
    private val viewModel: CloseReasonViewModel by viewModels()

    private lateinit var binding: DialogCloseReasonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCloseReasonBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.messageFlow
                .filterHandled()
                .collect {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launch {
            viewModel.closeReasonFlow
                .filterNotNull()
                .collect {
                    findNavController().apply {
                        sendDialogResult(
                            NavigationCode.CLOSE_REASON,
                            CloseReason(it.code, it.message)
                        )

                        popBackStack()
                    }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            etCloseReasonCode.doAfterTextChanged {
                try {
                    val code = etCloseReasonCode.text.toString().toInt()

                    bSendCloseReason.isEnabled = (code in 1000..4999)
                } catch (e: Exception) {
                    bSendCloseReason.isEnabled = false
                }
            }

            bSendCloseReason.setOnClickListener {
                val code = etCloseReasonCode.text.toString().trim().toInt()
                val message = etCloseReasonMessage.text.toString().trim()

                viewModel.onDisconnect(code, message)
            }
        }
    }
}
