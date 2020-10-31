package com.vastausf.wesolient.data

import java.util.*

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val source: Int,
    val content: String,
    val dateTime: Long
) {
    companion object {
        const val SERVER_SOURCE = 1
        const val CLIENT_SOURCE = 2
        const val SYSTEM_SOURCE = 3
    }
}
