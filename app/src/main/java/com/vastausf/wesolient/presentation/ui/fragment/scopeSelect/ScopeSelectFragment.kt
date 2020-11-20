package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vastausf.wesolient.R
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.presentation.ui.adapter.ScopeListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_scope.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class ScopeSelectFragment : MvpAppCompatFragment(R.layout.fragment_select_scope),
    ScopeSelectView {
    @Inject
    lateinit var presenterProvider: Provider<ScopeSelectPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
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
                                        presenter.onDeleteScope(item.uid)
                                    }

                                    return@setOnMenuItemClickListener true
                                }
                                else -> true
                            }
                        }
                    }.show()
                }
            )

            bCreate.setOnClickListener {
                presenter.onCreateScope()
            }

            bSettings.setOnClickListener {
                launchSettings()
            }
        }
    }

    override fun updateScopeList(scopeList: List<Scope>) {
        (rvScopeList.adapter as ScopeListAdapterRV).submitList(scopeList)

        if (scopeList.isNotEmpty()) {
            rvScopeList.visibility = View.VISIBLE
            tvScopeListPlaceholder.visibility = View.GONE
        } else {
            rvScopeList.visibility = View.INVISIBLE
            tvScopeListPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun showDeleteSnackbar(onClick: () -> Unit) {
        Snackbar.make(requireView(), R.string.delete_this_scope, Snackbar.LENGTH_LONG).apply {
            setAction(R.string.delete_yes) {
                onClick()
            }
            show()
        }
    }

    override fun showCreateDialog() {
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
