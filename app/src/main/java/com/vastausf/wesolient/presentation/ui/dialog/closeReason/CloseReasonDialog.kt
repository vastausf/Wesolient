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
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.sendDialogResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_close_reason.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CloseReasonDialog : MvpBottomSheetDialogFragment(), CloseReasonView {
    @Inject
    lateinit var presenterProvider: Provider<CloseReasonPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_close_reason, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            bApply.setOnClickListener {
                val code = etCode.text.toString().trim().toInt()
                val message = etMessage.text.toString().trim()

                presenter.onDisconnect(code, message)
            }

            etCode.doAfterTextChanged {
                try {
                    val code = etCode.text.toString().toInt()

                    bApply.isEnabled = (code in 1000..4999)
                } catch (e: Exception) {
                    bApply.isEnabled = false
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
        Toast.makeText(context, R.string.used_reserved_code, Toast.LENGTH_SHORT).show()
    }
}
