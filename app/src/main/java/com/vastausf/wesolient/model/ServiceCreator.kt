package com.vastausf.wesolient.model

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ServiceCreator {
    fun create(
        url: String,
        reconnectCount: Int
    ): ServiceHolder {
        val lifecycleRegistry = LifecycleRegistry()

        val okHttpClient = OkHttpClient
            .Builder()
            .pingInterval(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
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
            lifecycleRegistry,
            reconnectCount
        )
    }

    data class ServiceHolder(
        val service: SocketService,
        private val lifecycleRegistry: LifecycleRegistry,
        var reconnectCount: Int
    ) {
        fun connect() {
            lifecycleRegistry.onNext(Lifecycle.State.Started)
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
