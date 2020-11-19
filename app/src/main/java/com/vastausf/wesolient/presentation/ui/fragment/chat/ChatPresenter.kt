package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.vastausf.wesolient.Wesolient
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.data.Message
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.ValueListener
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val serviceCreator: ServiceCreator,
    private val wesolient: Wesolient
) : MvpPresenter<ChatView>() {
    private lateinit var scope: Scope

    var serviceHolder: ServiceCreator.ServiceHolder? = null

    private val messageList: MutableList<Message> = mutableListOf()

    fun onMessageSend(message: String) {
        if (message.isNotBlank()) {
            clientMessage(message)

            viewState.onSend()
        }
    }

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value
            }

            override fun onNotFound() {
                viewState.onMissScope()
            }

            override fun onFailure() {
                viewState.onMissScope()
            }
        })
    }

    fun onConnect() {
        serviceHolder = serviceCreator.create(scope.url)

        subscribeOnService()

        serviceHolder?.connect()

        viewState.changeConnectionState(true)
    }

    fun onDisconnect(code: Int? = null, reason: String? = null) {
        serviceHolder?.disconnect(code, reason)

        viewState.changeConnectionState(false)
    }

    private fun subscribeOnService() {
        presenterScope.launch {
            serviceHolder?.apply {
                service
                    .observeMessage()
                    .subscribe {
                        serverMessage(it)
                    }
            }
        }
    }

    private fun addNewMessage(content: String, source: Message.Source) {
        val message = Message(
            source = source,
            content = content,
            dateTime = 0
        )

        messageList.add(message)

        viewState.updateChatHistory(messageList)
    }

    private fun clientMessage(message: String) {
        serviceHolder?.service?.sendMessage(message)

        addNewMessage(message, Message.Source.CLIENT_SOURCE)
    }

    private fun systemMessage(message: String) {
        addNewMessage(message, Message.Source.SYSTEM_SOURCE)
    }

    private fun serverMessage(message: String) {
        addNewMessage(message, Message.Source.SERVER_SOURCE)
    }
}
