package com.vastausf.wesolient.model

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import com.vastausf.wesolient.data.client.*
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.flow.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ServiceCreator {
    fun create(
        url: String,
        retryOnConnectionFailure: Boolean
    ): ServiceHolder {
        val lifecycleRegistry = LifecycleRegistry()

        val okHttpClient = OkHttpClient
            .Builder()
            .pingInterval(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retryOnConnectionFailure)
            .build()
            .newWebSocketFactory(url)

        val service = Scarlet
            .Builder()
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .webSocketFactory(okHttpClient)
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .lifecycle(lifecycleRegistry)
            .build()
            .create<SocketService>()

        return ServiceHolder(
            service,
            lifecycleRegistry
        )
    }

    class ServiceHolder(
        val service: SocketService,
        private val lifecycleRegistry: LifecycleRegistry
    ) {
        fun connect(): Flow<Message> {
            lifecycleRegistry.onNext(Lifecycle.State.Started)

            return service.observeWebSocketEvent().consumeAsFlow().map { event ->
                when (event) {
                    is WebSocket.Event.OnConnectionOpened<*> -> {
                        SystemMessage(
                            SystemCode.CONNECTION_OPENED
                        )
                    }
                    is WebSocket.Event.OnMessageReceived -> {
                        when (
                            val message = event.message
                        ) {
                            is com.tinder.scarlet.Message.Text -> {
                                ServerTextMessage(
                                    message.value
                                )
                            }
                            is com.tinder.scarlet.Message.Bytes -> {
                                ServerBytesMessage(
                                    message.value
                                )
                            }
                        }
                    }
                    is WebSocket.Event.OnConnectionFailed -> {
                        SystemMessage(
                            SystemCode.CONNECTION_FAILED
                        )
                    }
                    is WebSocket.Event.OnConnectionClosing -> {
                        SystemMessage(
                            SystemCode.CONNECTION_CLOSING
                        )
                    }
                    is WebSocket.Event.OnConnectionClosed -> {
                        SystemMessage(
                            SystemCode.CONNECTION_CLOSED
                        )
                    }
                }
            }
        }

        fun sendTextMessage(
            content: String
        ) {
            service.sendMessage(content)
        }

        fun disconnect(code: Int? = null, reason: String? = null) {
            if (code != null && reason != null)
                lifecycleRegistry.onNext(
                    Lifecycle.State.Stopped.WithReason(
                        ShutdownReason(
                            code,
                            reason
                        )
                    )
                )
            else
                lifecycleRegistry.onNext(Lifecycle.State.Stopped.WithReason(ShutdownReason.GRACEFUL))
        }
    }
}
