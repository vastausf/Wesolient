package com.vastausf.wesolient.model.data

data class Message(
    var source: Source,
    var content: String,
    var dateTime: Long
) {
    enum class Source {
        SERVER_SOURCE,
        CLIENT_SOURCE
    }
}
