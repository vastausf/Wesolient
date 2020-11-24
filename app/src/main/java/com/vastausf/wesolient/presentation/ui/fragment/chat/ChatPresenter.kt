package com.vastausf.wesolient.presentation.ui.fragment.chat

import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.getLocalSystemTimestamp
import com.vastausf.wesolient.model.ScopeStore
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.listener.ValueListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import java.net.SocketException
import java.util.*
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val serviceCreator: ServiceCreator
) : MvpPresenter<ChatView>() {
    private lateinit var scope: Scope

    private lateinit var serviceHolder: ServiceCreator.ServiceHolder

    private val frameList: MutableList<Frame> = mutableListOf()

    private lateinit var connectionDisposable: CompositeDisposable

    fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            clientMessage(message)

            viewState.onSend()
        }
    }

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid, object : ValueListener<Scope> {
            override fun onSuccess(value: Scope) {
                scope = value

                viewState.bindData(value)
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
            scope.apply {
                viewState.changeConnectionState(null)

                serviceHolder = serviceCreator.create(url)

                connectionDisposable = CompositeDisposable()

                subscribeOnService()

                serviceHolder.connect()
            }
        } catch (exception: Exception) {
            onConnectionException(exception)

            viewState.changeConnectionState(false)
        }
    }

    fun onDisconnect(code: Int? = null, reason: String? = null) {
        try {
            viewState.changeConnectionState(null)

            serviceHolder.disconnect(code, reason)
        } catch (exception: Exception) {
            onConnectionException(exception)

            viewState.changeConnectionState(true)
        }
    }

    private fun subscribeOnService() {
        serviceHolder
            .service
            .observeWebSocketEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                when (it) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        onConnectionOpened()
                    }
                    is WebSocket.Event.OnMessageReceived -> {
                        when (val message = it.message) {
                            is Message.Text -> {
                                serverMessage(message.value)
                            }
                            is Message.Bytes -> {

                            }
                        }
                    }
                    is WebSocket.Event.OnConnectionFailed -> {
                        onConnectionException(it.throwable)
                        onConnectionClosed()
                    }
                    is WebSocket.Event.OnConnectionClosing -> {

                    }
                    is WebSocket.Event.OnConnectionClosed -> {
                        viewState.onDisconnectWithReason(
                            it.shutdownReason.code,
                            it.shutdownReason.reason
                        )
                        onConnectionClosed()

                        connectionDisposable.dispose()
                    }
                }
            }
            .disposeOnDisconnect()
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

    private fun addNewMessage(content: String, source: Frame.Source) {
        val message = Frame(
            UUID.randomUUID().toString(),
            source = source,
            content = content,
            dateTime = getLocalSystemTimestamp()
        )

        frameList.add(message)

        viewState.updateChatHistory(frameList)
    }

    private fun clientMessage(message: String) {
        serviceHolder.service.sendMessage(message)

        addNewMessage(message, Frame.Source.CLIENT_SOURCE)
    }

    private fun serverMessage(message: String) {
        addNewMessage(message, Frame.Source.SERVER_SOURCE)
    }

    private fun Disposable.disposeOnDisconnect() {
        connectionDisposable.add(this)
    }

    override fun onDestroy() {
        onDisconnect()
    }
}
