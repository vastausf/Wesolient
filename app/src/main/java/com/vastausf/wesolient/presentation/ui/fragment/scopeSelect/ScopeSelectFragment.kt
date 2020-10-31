package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.Scope
import com.vastausf.wesolient.presentation.ui.adapter.ScopeListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_scope.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class ScopeSelectFragment : MvpAppCompatFragment(R.layout.fragment_select_scope), ScopeSelectView {
    @Inject
    lateinit var presenterProvider: Provider<ScopeSelectPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            scopeListRV.layoutManager = LinearLayoutManager(requireContext())
            scopeListRV.adapter = ScopeListAdapterRV(
                onClick = {
                    launchScope(it.id)
                },
                onLongClick = {
                    launchScopeEdit(it.id)
                }
            )

            createScopeB.setOnClickListener {
                presenter.onClickCreateNew()
            }

            scopeListSRL.setOnRefreshListener {
                presenter.onRefresh()
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

    override fun updateLoadState(newState: Boolean) {
        scopeListSRL.isRefreshing = newState
    }

    private fun launchScope(id: String) {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToChatFragment(id)
        )
    }

    private fun launchScopeEdit(id: String) {
        findNavController().navigate(
            ScopeSelectFragmentDirections.actionScopeSelectFragmentToEditScopeDialog(id)
        )
    }
}
