package com.vastausf.wesolient.data.common

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Template(
    var uid: String = UUID.randomUUID().toString(),
    var title: String = "",
    var message: String = ""
)