package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vastausf.wesolient.R
import com.vastausf.wesolient.data.client.CloseReason
import com.vastausf.wesolient.databinding.FragmentChatBinding
import com.vastausf.wesolient.filterHandled
import com.vastausf.wesolient.listenResult
import com.vastausf.wesolient.presentation.ui.NavigationCode
import com.vastausf.wesolient.presentation.ui.adapter.ChatAdapterRV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()

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
                viewModel.sendMessage(etMessage.text.toString())
            }

            bTemplates.setOnClickListener {
                launchTemplateDialog()
            }

            bTemplates.setOnLongClickListener {
                launchVariableDialog()

                return@setOnLongClickListener true
            }

            etMessage.doAfterTextChanged { text ->
                viewModel.messageField.value = text.toString()
                text?.isNotEmpty()?.let { isNotEmpty ->
                    sendVisibleState(isNotEmpty)
                }
            }

            bConnect.setOnClickListener {
                viewModel.onConnect()
            }

            bDisconnect.setOnClickListener {
                viewModel.onDisconnect()
            }

            bDisconnect.setOnLongClickListener {
                launchCloseReasonDialog()

                return@setOnLongClickListener true
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onStart(args.uid)
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.apply {
            launch {
                viewModel.messageField
                    .collect {
                        if (it != binding.etMessage.text.toString())
                            binding.etMessage.setText(it)
                    }
            }

            launch {
                viewModel.connectionErrorFlow
                    .filterHandled()
                    .collect { url ->
                        Toast.makeText(
                            context,
                            getString(R.string.chat_connection_error, url),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            launch {
                viewModel.illegalUrlError
                    .filterHandled()
                    .collect {
                        Toast.makeText(context, R.string.chat_illegal_url, Toast.LENGTH_SHORT).show()
                    }
            }

            launch {
                viewModel.undefinedErrorFlow
                    .filterHandled()
                    .collect {
                        Toast.makeText(context, R.string.chat_undefined_error, Toast.LENGTH_SHORT)
                            .show()
                    }
            }

            launch {
                viewModel.missScopeErrorFlow
                    .filterHandled()
                    .collect {
                        Toast.makeText(context, R.string.chat_miss_scope, Toast.LENGTH_SHORT).show()

                        findNavController()
                            .popBackStack()
                    }
            }

            launch {
                viewModel.chatHistory
                    .collect {
                        (binding.rvChat.adapter as ChatAdapterRV).submitList(it.toList())
                    }
            }

            launch {
                viewModel.titleField
                    .collect {
                        binding.tvScopeTitle.text = it
                    }
            }

            launch {
                viewModel.connectionState
                    .collect { newState ->
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
            }

            launch {
                viewModel.closeReason
                    .filterHandled()
                    .collect { closeReason ->
                        Toast.makeText(
                            context,
                            getString(
                                R.string.chat_connection_closed,
                                closeReason.code,
                                closeReason.message
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun launchCloseReasonDialog() {
        findNavController().apply {
            navigate(ChatFragmentDirections.actionChatFragmentToCloseReasonDialog())

            listenResult<CloseReason>(NavigationCode.CLOSE_REASON, viewLifecycleOwner) {
                viewModel.onDisconnect(it.code, it.message)
            }
        }
    }

    private fun launchTemplateDialog() {
        findNavController().apply {
            navigate(ChatFragmentDirections.actionChatFragmentToTemplateSelectDialog(viewModel.scope.uid))

            listenResult<String>(NavigationCode.TEMPLATE_CODE, viewLifecycleOwner) {
                viewModel.onTemplateSelect(it)
            }
        }
    }

    private fun launchVariableDialog() {
        findNavController()
            .navigate(ChatFragmentDirections.actionChatFragmentToVariableSelectDialog(viewModel.scope.uid))
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
}
