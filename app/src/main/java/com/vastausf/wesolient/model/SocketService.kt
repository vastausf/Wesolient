package com.vastausf.wesolient.model

import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel


interface SocketService {
    @Send
    fun sendMessage(message: String)

    @Receive
    fun observeMessage(): ReceiveChannel<String>
}
