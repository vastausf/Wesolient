package com.vastausf.wesolient.model.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Message(
    var id: String = UUID.randomUUID().toString(),
    var source: Source,
    var content: String,
    var dateTime: Long
) {
    enum class Source {
        SERVER_SOURCE,
        CLIENT_SOURCE,
        SYSTEM_SOURCE
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to id,
            "source" to source,
            "content" to content,
            "dateTime" to dateTime
        )
    }
}

