package com.vastausf.wesolient.data

data class Message(
    val id: Long,
    val source: Source,
    val message: String,
    val dateTime: Long
) {
    enum class Source(val type: Int) {
        Server(1),
        Client(2),
        System(3)
    }
}
