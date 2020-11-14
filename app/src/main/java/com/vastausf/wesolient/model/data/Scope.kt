package com.vastausf.wesolient.model.data

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Scope(
    var uid: String = UUID.randomUUID().toString(),
    var title: String = "",
    var url: String = "",
    var history: MutableList<Message> = mutableListOf()
)
