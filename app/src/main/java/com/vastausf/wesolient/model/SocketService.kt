package com.vastausf.wesolient.model

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface SocketService {
    @Send
    fun sendMessage(message: String)

    @Receive
    fun observeMessage(): Flowable<String>
}
