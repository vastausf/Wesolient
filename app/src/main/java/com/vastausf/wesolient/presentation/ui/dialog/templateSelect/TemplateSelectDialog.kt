package com.vastausf.wesolient.presentation.ui.dialog.templateSelect

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.databinding.DialogTemplatesSelectBinding
import com.vastausf.wesolient.filterHandled
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.presentation.ui.adapter.TemplateListAdapterRV
import com.vastausf.wesolient.sendDialogResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TemplateSelectDialog : BottomSheetDialogFragment() {
    private val viewModel: TemplateSelectViewModel by viewModels()

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
                    onLongClick = { item, itemView ->
                        PopupMenu(context, itemView).apply {
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
                                            viewModel.delete(item.uid)
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

        viewModel.onStart(args.scopeUid)

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
            viewModel.templateList
                .collect {
                    (binding.rvTemplateList.adapter as TemplateListAdapterRV).submitList(it)
                }
        }
    }

    private fun launchScopeEdit(templateUid: String) {
        findNavController()
            .navigate(
                TemplateSelectDialogDirections.actionTemplateSelectDialogToEditTemplateDialog(
                    viewModel.scope.uid,
                    templateUid
                )
            )
    }

    private fun showCreateDialog() {
        findNavController().navigate(
            TemplateSelectDialogDirections
                .actionTemplateSelectDialogToCreateTemplateDialog(viewModel.scope.uid)
        )
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(
            requireView(),
            R.string.select_template_delete_confirmation,
            Snackbar.LENGTH_LONG
        )
            .apply {
                setAction(R.string.select_template_delete_positive) {
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
