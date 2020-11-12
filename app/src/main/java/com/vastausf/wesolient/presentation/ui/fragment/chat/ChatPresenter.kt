package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.SocketService
import com.vastausf.wesolient.model.data.Message
import com.vastausf.wesolient.model.data.Scope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import okhttp3.OkHttpClient
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStoreRealm: ScopeStore,
    private val scarletBuilder: Scarlet.Builder
) : MvpPresenter<ChatView>() {
    private lateinit var scope: Scope
    private lateinit var service: SocketService

    private var messageList: MutableList<Message> = mutableListOf()

    fun provideScopeTitle(title: String) {
        scopeStoreRealm
            .getById(title)
            ?.let {
                scope = it

                messageList = scope.history
                updateMessageList()

                createService()
            } ?: viewState.showMessageMissScope()
    }

    fun onMessageSend(message: String) {
        if (message.isNotEmpty()) {
            clientMessage(message)

            viewState.onSend()
        }
    }

    private fun updateMessageList() {
        viewState.updateChatHistory(messageList.toList())
    }

    private fun clientMessage(message: String) {
        service.sendMessage(message)
        addNewMessage(message, Message.Source.CLIENT_SOURCE)
    }

    private fun systemMessage(message: String) {
        addNewMessage(message, Message.Source.SYSTEM_SOURCE)
    }

    private fun serverMessage(message: String) {
        addNewMessage(message, Message.Source.SERVER_SOURCE)
    }

    private fun createService() {
        service = scarletBuilder
            .webSocketFactory(
                OkHttpClient.Builder().build().newWebSocketFactory(scope.url)
            )
            .build()
            .create()

        subscribeOnService()
    }

    private fun addNewMessage(content: String, source: Message.Source) {
        val message = Message(
            source = source,
            content = content,
            dateTime = 0
        )

        messageList.add(message)

        scopeStoreRealm.addMessageInScopeHistory(scope.uid, message)

        updateMessageList()
    }

    private fun subscribeOnService() {
        presenterScope.launch {
            service
                .observeMessage()
                .consumeEach {
                    serverMessage(it)
                }
        }
    }
}
