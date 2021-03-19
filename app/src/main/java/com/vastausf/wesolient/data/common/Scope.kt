package com.vastausf.wesolient.data.common

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Scope : RealmObject() {
    @PrimaryKey
    var uid: String = UUID.randomUUID().toString()

    @Required
    var title: String = ""

    @Required
    var url: String = ""

    var templates: RealmList<Template> = RealmList()

    var variables: RealmList<Variable> = RealmList()
}
