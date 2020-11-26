package com.vastausf.wesolient.data.common

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Variable(
    var uid: String = UUID.randomUUID().toString(),
    var title: String = "",
    var value: String = ""
)