package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vastausf.wesolient.R
import com.vastausf.wesolient.presentation.ui.adapter.ScopeListAdapterRV
import com.vastausf.wesolient.presentation.ui.dialog.createScope.CreateScopeDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_scope.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class ScopeSelectFragment: MvpAppCompatFragment(R.layout.fragment_select_scope), ScopeSelectView {
    @Inject
    lateinit var presenterProvider: Provider<ScopeSelectPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scopeListRV.adapter = ScopeListAdapterRV {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        createScopeB.setOnClickListener {
            presenter.onClickCreateNew()
        }
    }

    override fun updateScopeList(scopeList: List<String>) {
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
        CreateScopeDialog().apply {
            show(this@ScopeSelectFragment.childFragmentManager, "CreateScopeDialog")
        }
    }
}
