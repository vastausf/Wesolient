package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.model.CloseReason
import com.vastausf.wesolient.presentation.ui.adapter.ChatAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class ChatFragment : MvpAppCompatFragment(R.layout.fragment_chat), ChatView {
    @Inject
    lateinit var presenterProvider: Provider<ChatPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<ChatFragmentArgs>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            rvChat.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    stackFromEnd = true
                }
                adapter = ChatAdapterRV()
            }

            bSend.setOnClickListener {
                presenter.sendMessage(etMessage.text.toString())
            }

            bConnect.setOnClickListener {
                presenter.onConnect()
            }

            bDisconnect.setOnClickListener {
                presenter.onDisconnect()
            }

            bDisconnect.setOnLongClickListener {
                launchCloseReasonDialog()

                return@setOnLongClickListener true
            }

            presenter.onStart(args.uid)
        }
    }

    private fun launchCloseReasonDialog() {
        findNavController().apply {
            navigate(ChatFragmentDirections.actionChatFragmentToCloseReasonDialog())

            currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<CloseReason>(CloseReason.key)
                ?.observe(viewLifecycleOwner) {
                    presenter.onDisconnect(it.code, it.message)
                }
        }
    }

    override fun bindData(scope: Scope) {
        tvScopeTitle.text = scope.title
    }

    override fun updateChatHistory(chatHistory: List<Frame>) {
        (rvChat.adapter as ChatAdapterRV).submitList(chatHistory.toList())

        if (!rvChat.canScrollVertically(1)) {
            rvChat.smoothScrollToPosition(chatHistory.size)
        }
    }

    override fun onMissScope() {
        Toast.makeText(context, R.string.miss_scope, Toast.LENGTH_SHORT).show()

        findNavController()
            .popBackStack()
    }

    override fun onSend() {
        etMessage.text.clear()
    }

    override fun changeConnectionState(newState: Boolean?) {
        if (newState != null) {
            llMessageBar.visibility = if (newState) View.VISIBLE else View.GONE
            bDisconnect.visibility = if (newState) View.VISIBLE else View.GONE

            bConnect.visibility = if (!newState) View.VISIBLE else View.GONE

            pbConnection.visibility = View.GONE
        } else {
            llMessageBar.visibility = View.GONE
            bDisconnect.visibility = View.GONE

            bConnect.visibility = View.GONE

            pbConnection.visibility = View.VISIBLE
        }
    }

    override fun onConnectionError() {
        Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show()
    }

    override fun onDisconnectWithReason(code: Int, reason: String) {
        Toast.makeText(
            context,
            getString(R.string.connection_closed_with_data, code, reason),
            Toast.LENGTH_SHORT
        ).show()
    }
}
