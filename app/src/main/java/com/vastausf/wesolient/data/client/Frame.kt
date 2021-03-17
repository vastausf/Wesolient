package com.vastausf.wesolient.data.client

data class Frame(
    var uid: String,
    var source: Source,
    var content: String,
    var dateTime: Long
) {
    enum class Source {
        SERVER,
        CLIENT,
        SYSTEM
    }
}
