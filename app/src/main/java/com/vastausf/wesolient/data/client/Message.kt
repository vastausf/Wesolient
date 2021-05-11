package com.vastausf.wesolient.data.client

import java.util.*


sealed class Message(
    val uid: String = UUID.randomUUID().toString(),
    var dateTime: Long = 0L
)

class ServerTextMessage(
    val content: String
) : Message()

class ServerBytesMessage(
    val content: ByteArray
) : Message()

class ClientMessage(
    val content: String,
    val templateUid: String?
) : Message()

class SystemMessage(
    val code: SystemCode
) : Message()

enum class SystemCode {
    CONNECTION_OPENING,
    CONNECTION_OPENED,
    CONNECTION_FAILED,
    CONNECTION_CLOSING,
    CONNECTION_CLOSED
}
