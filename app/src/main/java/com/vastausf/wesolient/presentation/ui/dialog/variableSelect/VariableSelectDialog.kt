package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

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
import com.vastausf.wesolient.databinding.DialogVariableSelectBinding
import com.vastausf.wesolient.presentation.ui.adapter.VariableListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VariableSelectDialog : BottomSheetDialogFragment() {
    private val viewModel: VariableSelectViewModel by viewModels()

    private val args by navArgs<VariableSelectDialogArgs>()

    private lateinit var binding: DialogVariableSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogVariableSelectBinding.inflate(LayoutInflater.from(context))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            binding.rvVariableList.apply {
                adapter = VariableListAdapterRV(
                    onLongClick = { item, _ ->
                        PopupMenu(context, view).apply {
                            inflate(R.menu.variable_menu)
                            gravity = Gravity.END
                            setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.editMI -> {
                                        launchVariableEdit(item.uid)

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

            binding.fabCreateVariable.setOnClickListener {
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
            viewModel.messageFlow.filterNotNull()
                .map {
                    it.getValueIfNotHandled()
                }.filterNotNull()
                .collect {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launch {
            viewModel.variableList
                .collect {
                    (binding.rvVariableList.adapter as VariableListAdapterRV).submitList(it)
                }
        }
    }

    private fun launchVariableEdit(variableUid: String) {
        findNavController()
            .navigate(
                VariableSelectDialogDirections.actionVariableSelectDialogToEditVariableDialog(
                    viewModel.scope.uid,
                    variableUid
                )
            )
    }

    private fun showCreateDialog() {
        findNavController().navigate(
            VariableSelectDialogDirections.actionVariableSelectDialogToCreateVariableDialog(
                viewModel.scope.uid
            )
        )
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(
            requireView(),
            R.string.select_variable_delete_confirmation,
            Snackbar.LENGTH_LONG
        )
            .apply {
                setAction(R.string.select_variable_delete_positive) {
                    onClick()
                }
                show()
            }
    }
}
