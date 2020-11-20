package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.tinder.scarlet.WebSocket
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.data.Message
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.ValueListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import java.net.SocketException
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val serviceCreator: ServiceCreator
) : MvpPresenter<ChatView>() {
    private var scope: Scope? = null

    private var serviceHolder: ServiceCreator.ServiceHolder? = null

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
        try {
            scope?.apply {
                serviceHolder = serviceCreator.create(url)

                subscribeOnService()

                serviceHolder?.connect()
            }
        } catch (exception: Exception) {
            onConnectionException(exception)
        }
    }

    fun onDisconnect(code: Int? = null, reason: String? = null) {
        try {
            serviceHolder?.disconnect(code, reason)
        } catch (exception: Exception) {
            onConnectionException(exception)
        }
    }

    private fun subscribeOnService() {
        serviceHolder?.apply {
            presenterScope.launch {
                service
                    .observeMessage()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        serverMessage(it)
                    }
            }

            service
                .observeWebSocketEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    when (it) {
                        is WebSocket.Event.OnConnectionOpened<*> -> {
                            onConnectionOpened()
                        }
                        is WebSocket.Event.OnMessageReceived -> {

                        }
                        is WebSocket.Event.OnConnectionFailed -> {
                            onConnectionException(it.throwable)
                            onConnectionClosed()
                        }
                        is WebSocket.Event.OnConnectionClosing -> {

                        }
                        is WebSocket.Event.OnConnectionClosed -> {
                            onConnectionClosed()
                        }
                    }
                }
        }
    }

    private fun onConnectionException(exception: Throwable) {
        exception.printStackTrace()

        when (exception) {
            is SocketException -> {
                viewState.onConnectionError()
            }
            else -> {
                viewState.onConnectionError()
            }
        }
    }

    private fun onConnectionOpened() {
        viewState.changeConnectionState(true)
    }

    private fun onConnectionClosed() {
        viewState.changeConnectionState(false)
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

    private fun serverMessage(message: String) {
        addNewMessage(message, Message.Source.SERVER_SOURCE)
    }

    override fun onDestroy() {
        onDisconnect()
    }
}
