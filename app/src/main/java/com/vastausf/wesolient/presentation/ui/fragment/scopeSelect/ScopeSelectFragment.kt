package com.vastausf.wesolient.presentation.ui.fragment.scopeSelect

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vastausf.wesolient.R
import com.vastausf.wesolient.databinding.FragmentSelectScopeBinding
import com.vastausf.wesolient.filterHandled
import com.vastausf.wesolient.presentation.design.WesolientTheme
import com.vastausf.wesolient.presentation.ui.adapter.ScopeListAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun ScopeList(viewModel: ScopeSelectViewModel = viewModel()) {
    val scopeList = viewModel.scopeList.collectAsState()

    ApplicationHeader()
}

@Preview(showBackground = true)
@Composable
fun ApplicationHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp, 32.dp),
            painter = painterResource(id = R.drawable.ic_app),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        Text(
            text = stringResource(id = R.string.app_head_name),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary
        )
    }
}

@AndroidEntryPoint
class ScopeSelectFragment : Fragment() {
    private val viewModel: ScopeSelectViewModel by viewModels()

    private lateinit var binding: FragmentSelectScopeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onCreate()
    }

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

        lifecycleScope.apply {
            launch {
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

            launch {
                viewModel.createDialogState
                    .filterHandled()
                    .collect {
                        showCreateDialog()
                    }
            }
        }

        return binding.root
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

    private fun showCreateDialog() {
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
