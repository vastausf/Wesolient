package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.client.CloseReason
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.databinding.FragmentChatBinding
import com.vastausf.wesolient.listenResult
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.presentation.ui.adapter.ChatAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class ChatFragment : MvpAppCompatFragment(), ChatView {
    @Inject
    lateinit var presenterProvider: Provider<ChatPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private val args by navArgs<ChatFragmentArgs>()

    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            rvChat.apply {
                layoutManager = LinearLayoutManager(context).apply {
                    stackFromEnd = true
                }
                adapter = ChatAdapterRV()
            }

            bSend.setOnClickListener {
                presenter.sendMessage(etMessage.text.toString())
            }

            bTemplates.setOnClickListener {
                launchTemplateDialog()
            }

            bTemplates.setOnLongClickListener {
                launchVariableDialog()

                return@setOnLongClickListener true
            }

            etMessage.doAfterTextChanged { text ->
                text?.isNotEmpty()?.let { isNotEmpty ->
                    sendVisibleState(isNotEmpty)
                }
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
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        presenter.onStart(args.uid)
    }

    private fun launchCloseReasonDialog() {
        findNavController().apply {
            navigate(ChatFragmentDirections.actionChatFragmentToCloseReasonDialog())

            listenResult<CloseReason>(NavigationCode.CLOSE_REASON, viewLifecycleOwner) {
                presenter.onDisconnect(it.code, it.message)
            }
        }
    }

    private fun launchTemplateDialog() {
        findNavController().apply {
            navigate(ChatFragmentDirections.actionChatFragmentToTemplateSelectDialog(presenter.scope.uid))

            listenResult<String>(NavigationCode.TEMPLATE_CODE, viewLifecycleOwner) {
                presenter.onTemplateSelect(it)
            }
        }
    }

    private fun launchVariableDialog() {
        findNavController()
            .navigate(ChatFragmentDirections.actionChatFragmentToVariableSelectDialog(presenter.scope.uid))
    }

    private fun messageBarVisibleState(isVisible: Boolean) {
        binding.apply {
            if (isVisible) {
                llMessageBar.visibility = View.VISIBLE
            } else {
                llMessageBar.visibility = View.GONE
            }
        }
    }

    private fun connectionVisibleState(isVisible: Boolean?) {
        binding.apply {
            if (isVisible != null) {
                if (isVisible) {
                    bDisconnect.visibility = View.VISIBLE

                    bConnect.visibility = View.GONE
                } else {
                    bDisconnect.visibility = View.GONE

                    bConnect.visibility = View.VISIBLE
                }
            } else {
                bConnect.visibility = View.GONE

                bDisconnect.visibility = View.GONE
            }
        }
    }

    private fun sendVisibleState(isVisible: Boolean) {
        binding.apply {
            if (isVisible) {
                bSend.visibility = View.VISIBLE

                bTemplates.visibility = View.GONE
            } else {
                bSend.visibility = View.GONE

                bTemplates.visibility = View.VISIBLE
            }
        }
    }

    override fun bindData(scope: Scope) {
        binding.apply {
            tvScopeTitle.text = scope.title
        }
    }

    override fun updateChatHistory(chatHistory: List<Frame>) {
        binding.apply {
            (rvChat.adapter as ChatAdapterRV).submitList(chatHistory.toList())

            if (!rvChat.canScrollVertically(1)) {
                rvChat.smoothScrollToPosition(chatHistory.size)
            }
        }
    }

    override fun onMissScope() {
        Toast.makeText(context, R.string.chat_miss_scope, Toast.LENGTH_SHORT).show()

        findNavController()
            .popBackStack()
    }

    override fun bindMessageTemplate(template: String) {
        binding.apply {
            etMessage.setText(template)
        }
    }

    override fun onSend() {
        binding.apply {
            etMessage.text.clear()
        }
    }

    override fun changeConnectionState(newState: Boolean?) {
        binding.apply {
            if (newState != null) {
                messageBarVisibleState(newState)

                connectionVisibleState(newState)

                pbConnection.visibility = View.GONE
            } else {
                messageBarVisibleState(false)

                connectionVisibleState(null)

                pbConnection.visibility = View.VISIBLE
            }
        }
    }

    override fun onConnectionError() {
        Toast.makeText(context, R.string.chat_connection_error, Toast.LENGTH_SHORT).show()
    }

    override fun onDisconnectWithReason(code: Int, reason: String) {
        Toast.makeText(
            context,
            getString(R.string.chat_connection_closed, code, reason),
            Toast.LENGTH_SHORT
        ).show()
    }
}
