package com.vastausf.wesolient.presentation.ui.dialog.editScope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_edit_scope.*
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class EditScopeDialog : MvpBottomSheetDialogFragment(), EditScopeDialogView {
    @Inject
    lateinit var presenterProvider: Provider<EditScopePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<EditScopeDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_scope, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            bApply.setOnClickListener {
                val newTitle = etCode.text.toString().trim()
                val newUrl = etMessage.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            etCode.doAfterTextChanged {
                bApply.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.uid)
    }

    override fun bindField(title: String, url: String) {
        view.apply {
            etCode.setText(title)
            etMessage.setText(url)
        }
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }
}
