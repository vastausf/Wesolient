package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vastausf.wesolient.R
import com.vastausf.wesolient.model.data.Message
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
            chatRV.apply {
                layoutManager = LinearLayoutManager(requireContext()).apply {
                    stackFromEnd = true
                }
                adapter = ChatAdapterRV()
            }

            sendB.setOnClickListener {
                presenter.onMessageSend(messageET.text.toString())
            }
        }

        presenter.provideScopeTitle(args.scopeTitle)
    }

    override fun updateChatHistory(chatHistory: List<Message>) {
        (chatRV.adapter as ChatAdapterRV).submitList(chatHistory)
        if (!chatRV.canScrollVertically(1)) {
            chatRV.smoothScrollToPosition(chatHistory.size)
        }
    }

    override fun showMessageMissScope() {
        Toast.makeText(context, R.string.miss_scope, Toast.LENGTH_SHORT).show()
    }

    override fun onSend() {
        messageET.text.clear()
    }
}
