package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.databinding.DialogTemplatesSelectBinding
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.presentation.ui.adapter.TemplateListAdapterRV
import com.vastausf.wesolient.sendDialogResult
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class TemplateSelectDialog : MvpBottomSheetDialogFragment(), TemplateSelectView {
    @Inject
    lateinit var presenterProvider: Provider<TemplateSelectPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<TemplateSelectDialogArgs>()

    private lateinit var binding: DialogTemplatesSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTemplatesSelectBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            binding.rvTemplateList.apply {
                adapter = TemplateListAdapterRV(
                    onClick = { item, _ ->
                        onTemplateSelect(item)
                    },
                    onLongClick = { item, _ ->
                        PopupMenu(context, view).apply {
                            inflate(R.menu.template_menu)
                            gravity = Gravity.END
                            setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.editMI -> {
                                        launchScopeEdit(item.uid)

                                        return@setOnMenuItemClickListener true
                                    }
                                    R.id.deleteMI -> {
                                        showDeleteSnackbar {
                                            presenter.onDeleteTemplate(item.uid)
                                        }

                                        return@setOnMenuItemClickListener true
                                    }
                                    else -> true
                                }
                            }
                        }.show()
                    }
                )
                layoutManager = LinearLayoutManager(context)
            }

            binding.fabCreateTemplate.setOnClickListener {
                showCreateDialog()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.scopeUid)
    }

    override fun bindTemplateList(templateList: List<Template>) {
        view?.apply {
            (binding.rvTemplateList.adapter as TemplateListAdapterRV).submitList(templateList)
        }
    }

    override fun onDeleteSuccess() {
        Toast.makeText(context, R.string.template_delete_success, Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteFailure() {
        Toast.makeText(context, R.string.template_delete_failure, Toast.LENGTH_SHORT).show()
    }

    private fun launchScopeEdit(templateUid: String) {
        findNavController()
            .navigate(
                TemplateSelectDialogDirections.actionTemplateSelectDialogToEditTemplateDialog(
                    presenter.scope.uid,
                    templateUid
                )
            )
    }

    private fun showCreateDialog() {
        findNavController().navigate(
            TemplateSelectDialogDirections
                .actionTemplateSelectDialogToCreateTemplateDialog(presenter.scope.uid)
        )
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(requireView(), R.string.delete_template_confirmation, Snackbar.LENGTH_LONG)
            .apply {
                setAction(R.string.delete_scope_positive) {
                    onClick()
                }
                show()
            }
    }

    private fun onTemplateSelect(template: Template) {
        findNavController().apply {
            sendDialogResult(NavigationCode.TEMPLATE_CODE, template.uid)

            popBackStack()
        }
    }
}
