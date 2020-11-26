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
            fabCreateScope.setOnClickListener {
                val title = etTitle.text.toString().trim()
                val url = etUrl.text.toString().trim()

                presenter.onNewScopeCreate(title, url)
            }

            etTitle.doAfterTextChanged {
                fabCreateScope.isEnabled = it.toString().isNotBlank()
            }
        }
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun showErrorMessage() {
        Toast.makeText(context, R.string.scope_create_failure, Toast.LENGTH_SHORT).show()
    }
}
