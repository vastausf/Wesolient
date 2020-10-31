package com.vastausf.wesolient.model.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class MessageRealm(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var source: Int = -1,
    var content: String = "",
    var dateTime: Long = 0
) : RealmObject()
