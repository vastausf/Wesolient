package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
            scopeListRV.layoutManager = LinearLayoutManager(requireContext())
            scopeListRV.adapter = ScopeListAdapterRV(
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
                                    launchScopeDelete(item.uid)

                                    return@setOnMenuItemClickListener true
                                }
                                else -> true
                            }
                        }
                    }.show()
                }
            )

            createScopeB.setOnClickListener {
                presenter.onClickCreateNew()
            }

            settingsB.setOnClickListener {
                launchSettings()
            }
        }
    }

    override fun updateScopeList(scopeList: List<Scope>) {
        (scopeListRV.adapter as ScopeListAdapterRV).submitList(scopeList)

        if (scopeList.isNotEmpty()) {
            scopeListRV.visibility = View.VISIBLE
            scopeListPlaceholder.visibility = View.GONE
        } else {
            scopeListRV.visibility = View.GONE
            scopeListPlaceholder.visibility = View.VISIBLE
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

    private fun launchScopeDelete(uid: String) {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToDeleteScopeDialog(uid)
        )
    }

    private fun launchSettings() {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToSettingsFragment()
        )
    }
}
