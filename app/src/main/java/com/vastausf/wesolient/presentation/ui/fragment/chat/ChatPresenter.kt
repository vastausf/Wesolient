package com.vastausf.wesolient.presentation.ui.fragment.chat

import android.util.Log
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.vastausf.wesolient.data.client.Frame
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.exception.IllegalUrlException
import com.vastausf.wesolient.getLocalSystemTimestamp
import com.vastausf.wesolient.model.ServiceCreator
import com.vastausf.wesolient.model.store.ScopeStore
import com.vastausf.wesolient.model.store.SettingsStore
import com.vastausf.wesolient.model.store.TemplateStore
import com.vastausf.wesolient.model.store.VariableStore
import com.vastausf.wesolient.replaceVariables
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import java.net.ConnectException
import java.net.SocketException
import java.util.*
import javax.inject.Inject

@InjectViewState
class ChatPresenter
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore,
    private val variableStore: VariableStore,
    private val settingsStore: SettingsStore,
    private val serviceCreator: ServiceCreator
) : MvpPresenter<ChatView>() {
    lateinit var scope: Scope
    lateinit var settings: Settings

    private lateinit var serviceHolder: ServiceCreator.ServiceHolder

    private val frameList: MutableList<Frame> = mutableListOf()

    private lateinit var connectionDisposable: CompositeDisposable

    private val wsPrefix: String = "ws://"
    private val wssPrefix: String = "wss://"

    fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            clientMessage(message)

            viewState.onSend()
        }
    }

    fun onTemplateSelect(uid: String) {
        templateStore.getTemplateOnce(scope.uid, uid) { template ->
            viewState.bindMessageTemplate(template.message)
        }
    }

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid,
            onSuccess = { value ->
                scope = value

                viewState.bindData(value)

                settingsStore.getSettingsOnce { settings ->
                    if (settings.autoConnect) {
                        onConnect()
                    }
                }
            },
            onNotFound = {
                viewState.onMissScope()
            },
            onFailure = {
                viewState.onMissScope()
            }
        )

        settingsStore.onSettingsUpdate { settings ->
            this.settings = settings
        }
    }

    fun onConnect() {
        try {
            scope.apply {
                viewState.changeConnectionState(null)

                if (!(url.startsWith(wsPrefix, true) || url.startsWith(wssPrefix, true)))
                    throw IllegalUrlException()

                serviceHolder = serviceCreator.create(url, settings.reconnectCount)

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
                                serverMessage(message.value.toString())
                            }
                        }
                    }
                    is WebSocket.Event.OnConnectionFailed -> {
                        onConnectionException(it.throwable)
                        onConnectionClosed()

                        Log.d("reconnectCount", serviceHolder.reconnectCount.toString())
                        if (serviceHolder.reconnectCount-- == 0) {
                            serviceHolder.disconnect()
                        }
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
            is ConnectException -> {
                viewState.onConnectionError(scope.url)
            }
            is IllegalUrlException -> {
                viewState.onIllegalUrl()
            }
            else -> {
                viewState.onUndefinedError()
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
        variableStore.getVariableListOnce(scope.uid) { variableList ->
            val replacedMessage = message.replaceVariables(variableList)

            serviceHolder.service.sendMessage(replacedMessage)

            addNewMessage(replacedMessage, Frame.Source.CLIENT_SOURCE)
        }
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
