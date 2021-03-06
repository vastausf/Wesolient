package com.vastausf.wesolient.presentation.ui.fragment.chat

import androidx.lifecycle.ViewModel
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.vastausf.wesolient.SingleEvent
import com.vastausf.wesolient.data.client.CloseReason
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
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.ConnectException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject
constructor(
    private val scopeStore: ScopeStore,
    private val templateStore: TemplateStore,
    private val variableStore: VariableStore,
    private val settingsStore: SettingsStore,
    private val serviceCreator: ServiceCreator
) : ViewModel() {
    val messageField = MutableStateFlow("")

    private val _titleField = MutableStateFlow("")
    val titleField = _titleField.asStateFlow()

    private val _connectionState = MutableStateFlow<Boolean?>(false)
    val connectionState = _connectionState.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<Frame>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    //Single event flows
    private val _connectionErrorFlow = MutableStateFlow<SingleEvent<String>?>(null)
    val connectionErrorFlow = _connectionErrorFlow.asStateFlow()

    private val _illegalUrlErrorFlow = MutableStateFlow<SingleEvent<String>?>(null)
    val illegalUrlError = _illegalUrlErrorFlow.asStateFlow()

    private val _undefinedErrorFlow = MutableStateFlow<SingleEvent<String>?>(null)
    val undefinedErrorFlow = _undefinedErrorFlow.asStateFlow()

    private val _missScopeErrorFlow = MutableStateFlow<SingleEvent<String>?>(null)
    val missScopeErrorFlow = _missScopeErrorFlow.asStateFlow()

    private val _closeReason = MutableStateFlow<SingleEvent<CloseReason>?>(null)
    val closeReason = _closeReason.asStateFlow()


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

            messageField.value = ""
        }
    }

    fun onTemplateSelect(uid: String) {
        templateStore.getTemplateOnce(scope.uid, uid) { template ->
            messageField.value = template.message
        }
    }

    fun onStart(uid: String) {
        scopeStore.getScopeOnce(uid,
            onSuccess = { value ->
                scope = value

                _titleField.value = value.title

                settingsStore.getSettingsOnce { settings ->
                    this.settings = settings

                    if (settings.autoConnect) {
                        onConnect()
                    }
                }
            },
            onNotFound = {
                _missScopeErrorFlow.value = SingleEvent("")
            },
            onFailure = {
                _missScopeErrorFlow.value = SingleEvent("")
            }
        )
    }

    fun onConnect() {
        try {
            scope.apply {
                onConnectionChange()

                if (!(url.startsWith(wsPrefix, true) || url.startsWith(wssPrefix, true)))
                    throw IllegalUrlException()

                serviceHolder = serviceCreator.create(url, settings.reconnectCount)

                connectionDisposable = CompositeDisposable()

                subscribeOnService()

                serviceHolder.connect()
            }
        } catch (exception: Exception) {
            onConnectionException(exception)

            onConnectionClosed()
        }
    }

    fun onDisconnect(code: Int? = null, reason: String? = null) {
        try {
            onConnectionChange()

            if (connectionState.value == true) {
                serviceHolder.disconnect(code, reason)
            }
        } catch (exception: Exception) {
            onConnectionException(exception)

            onConnectionOpened()
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

                        if (serviceHolder.reconnectCount-- == 0) {
                            serviceHolder.disconnect()
                        }
                    }
                    is WebSocket.Event.OnConnectionClosing -> {

                    }
                    is WebSocket.Event.OnConnectionClosed -> {
                        _closeReason.value = SingleEvent(
                            CloseReason(
                                it.shutdownReason.code,
                                it.shutdownReason.reason
                            )
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
                _connectionErrorFlow.value = SingleEvent(scope.url)
            }
            is IllegalUrlException -> {
                _illegalUrlErrorFlow.value = SingleEvent("")
            }
            else -> {
                _undefinedErrorFlow.value = SingleEvent("")
            }
        }
    }

    private fun onConnectionOpened() {
        _connectionState.value = true
    }

    private fun onConnectionChange() {
        _connectionState.value = null
    }

    private fun onConnectionClosed() {
        _connectionState.value = false
    }

    private fun addNewMessage(content: String, source: Frame.Source) {
        val message = Frame(
            UUID.randomUUID().toString(),
            source = source,
            content = content,
            dateTime = getLocalSystemTimestamp()
        )

        frameList.add(message)

        _chatHistory.value = frameList.toList()
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

    override fun onCleared() {
        onDisconnect()
    }
}
