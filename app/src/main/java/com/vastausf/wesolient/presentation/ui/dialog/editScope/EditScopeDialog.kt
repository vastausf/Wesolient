package com.vastausf.wesolient.presentation.ui.dialog.editScope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vastausf.wesolient.databinding.DialogEditScopeBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class EditScopeDialog : MvpBottomSheetDialogFragment(), EditScopeView {
    @Inject
    lateinit var presenterProvider: Provider<EditScopePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<EditScopeDialogArgs>()

    private lateinit var binding: DialogEditScopeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditScopeBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bApply.setOnClickListener {
                val newTitle = etScopeTitle.text.toString().trim()
                val newUrl = etScopeUrl.text.toString().trim()

                presenter.onApply(
                    newTitle,
                    newUrl
                )
            }

            etScopeTitle.doAfterTextChanged {
                bApply.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.uid)
    }

    override fun bindField(title: String, url: String) {
        binding.apply {
            etScopeTitle.setText(title)
            etScopeUrl.setText(url)
        }
    }

    override fun onApplySuccess() {
        findNavController().popBackStack()
    }
}
