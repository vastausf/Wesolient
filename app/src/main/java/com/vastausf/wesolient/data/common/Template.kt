package com.vastausf.wesolient.data.common

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Template : RealmObject() {
    @PrimaryKey
    var uid: String = UUID.randomUUID().toString()

    @Required
    var title: String = ""

    @Required
    var message: String = ""
}
