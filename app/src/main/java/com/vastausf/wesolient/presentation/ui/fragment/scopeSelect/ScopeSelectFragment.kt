package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.FragmentSelectScopeBinding
import com.vastausf.wesolient.presentation.ui.adapter.ScopeListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScopeSelectFragment : Fragment() {
    private val viewModel: ScopeSelectViewModel by viewModels()

    private lateinit var binding: FragmentSelectScopeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectScopeBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            rvScopeList.layoutManager = LinearLayoutManager(requireContext())
            rvScopeList.adapter = ScopeListAdapterRV(
                onClick = { item, _ ->
                    launchScopeChat(item.uid)
                },
                onLongClick = { item, view ->
                    PopupMenu(context, view).apply {
                        inflate(R.menu.scope_menu)
                        gravity = Gravity.END
                        setOnMenuItemClickListener {
                            when (it.itemId) {
                                R.id.editMI -> {
                                    launchScopeEdit(item.uid)

                                    return@setOnMenuItemClickListener true
                                }
                                R.id.deleteMI -> {
                                    showDeleteSnackbar {
                                        viewModel.deleteScope(item.uid)
                                    }

                                    return@setOnMenuItemClickListener true
                                }
                                else -> true
                            }
                        }
                    }.show()
                }
            )

            fabCreateScope.setOnClickListener {
                viewModel.showCreateDialog()
            }

            bSettings.setOnClickListener {
                launchSettings()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.onStart()

        lifecycleScope.launch {
            viewModel.scopeList
                .collect { scopeList ->
                    binding.apply {
                        (rvScopeList.adapter as ScopeListAdapterRV).submitList(scopeList)

                        if (scopeList.isNotEmpty()) {
                            rvScopeList.visibility = View.VISIBLE
                            tvScopeListPlaceholder.visibility = View.GONE
                        } else {
                            rvScopeList.visibility = View.INVISIBLE
                            tvScopeListPlaceholder.visibility = View.VISIBLE
                        }
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.createDialogState
                .map {
                    it?.getValueIfNotHandled()
                }.filterNotNull()
                .collect {
                    showCreateDialog()
                }
        }
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(requireView(), R.string.delete_scope_confirmation, Snackbar.LENGTH_LONG)
            .apply {
                setAction(R.string.delete_scope_positive) {
                    onClick()
                }
                show()
            }
    }

    fun showCreateDialog() {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToCreateScopeDialog()
        )
    }

    private fun launchScopeChat(uid: String) {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToChatFragment(uid)
        )
    }

    private fun launchScopeEdit(uid: String) {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToEditScopeDialog(uid)
        )
    }

    private fun launchSettings() {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToSettingsFragment()
        )
    }
}
