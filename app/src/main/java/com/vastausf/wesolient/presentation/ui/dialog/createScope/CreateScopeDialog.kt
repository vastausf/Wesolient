package com.vastausf.wesolient.presentation.ui.dialog.createScope

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.DialogCreateScopeBinding
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CreateScopeDialog : MvpBottomSheetDialogFragment(), CreateScopeView {
    @Inject
    lateinit var presenterProvider: Provider<CreateScopePresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var binding: DialogCreateScopeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateScopeBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            bCreateScope.setOnClickListener {
                val title = etScopeTitle.text.toString().trim()
                val url = etScopeUrl.text.toString().trim()

                presenter.onNewScopeCreate(title, url)
            }

            etScopeTitle.doAfterTextChanged {
                bCreateScope.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun showErrorMessage() {
        Toast.makeText(context, R.string.create_scope_failure, Toast.LENGTH_SHORT).show()
    }
}
