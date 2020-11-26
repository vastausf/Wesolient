package com.vastausf.wesolient.presentation.ui.dialog.closeReason

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.client.CloseReason
import com.vastausf.wesolient.databinding.DialogCloseReasonBinding
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.sendDialogResult
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CloseReasonDialog : MvpBottomSheetDialogFragment(), CloseReasonView {
    @Inject
    lateinit var presenterProvider: Provider<CloseReasonPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var binding: DialogCloseReasonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCloseReasonBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bSendCloseReason.setOnClickListener {
                val code = etCloseReasonCode.text.toString().trim().toInt()
                val message = etCloseReasonMessage.text.toString().trim()

                presenter.onDisconnect(code, message)
            }

            etCloseReasonCode.doAfterTextChanged {
                try {
                    val code = etCloseReasonCode.text.toString().toInt()

                    bSendCloseReason.isEnabled = (code in 1000..4999)
                } catch (e: Exception) {
                    bSendCloseReason.isEnabled = false
                }
            }
        }
    }

    override fun sendCloseReason(code: Int, message: String) {
        findNavController().apply {
            sendDialogResult(NavigationCode.CLOSE_REASON, CloseReason(code, message))

            popBackStack()
        }
    }

    override fun onUsedReservedCode() {
        Toast.makeText(context, R.string.close_reason_used_reserved_code, Toast.LENGTH_SHORT).show()
    }
}
