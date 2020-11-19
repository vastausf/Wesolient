package com.vastausf.wesolient.model

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient

class ServiceCreator {
    fun create(
        url: String
    ): ServiceHolder {
        val lifecycleRegistry = LifecycleRegistry()

        val service = Scarlet
            .Builder()
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .webSocketFactory(OkHttpClient.Builder().build().newWebSocketFactory(url))
            .lifecycle(lifecycleRegistry)
            .build()
            .create<SocketService>()

        return ServiceHolder(
            service,
            lifecycleRegistry
        )
    }

    data class ServiceHolder(
        val service: SocketService,
        private val lifecycleRegistry: LifecycleRegistry
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
                lifecycleRegistry.onNext(Lifecycle.State.Stopped.AndAborted)
        }
    }
}
