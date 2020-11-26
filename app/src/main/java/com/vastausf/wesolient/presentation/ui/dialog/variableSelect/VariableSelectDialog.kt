package com.vastausf.wesolient.presentation.ui.dialog.variableSelect

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
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.databinding.DialogVariableSelectBinding
import com.vastausf.wesolient.presentation.ui.adapter.VariableListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpBottomSheetDialogFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class VariableSelectDialog : MvpBottomSheetDialogFragment(), VariableSelectView {
    @Inject
    lateinit var presenterProvider: Provider<VariableSelectPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

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
                                            presenter.onDelete(item.uid)
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

        presenter.onStart(args.scopeUid)
    }

    override fun bindVariableList(variableList: List<Variable>) {
        view?.apply {
            (binding.rvVariableList.adapter as VariableListAdapterRV).submitList(variableList)
        }
    }

    override fun onDeleteSuccess() {
        Toast.makeText(context, R.string.delete_variable_success, Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteFailure() {
        Toast.makeText(context, R.string.delete_variable_failure, Toast.LENGTH_SHORT).show()
    }

    private fun launchVariableEdit(variableUid: String) {
        findNavController()
            .navigate(
                VariableSelectDialogDirections.actionVariableSelectDialogToEditVariableDialog(
                    presenter.scope.uid,
                    variableUid
                )
            )
    }

    private fun showCreateDialog() {
        findNavController().navigate(
            VariableSelectDialogDirections.actionVariableSelectDialogToCreateVariableDialog(
                presenter.scope.uid
            )
        )
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(requireView(), R.string.delete_variable_confirmation, Snackbar.LENGTH_LONG)
            .apply {
                setAction(R.string.delete_variable_positive) {
                    onClick()
                }
                show()
            }
    }
}
