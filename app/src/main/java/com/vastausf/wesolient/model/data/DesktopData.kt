package com.vastausf.wesolient.model.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DesktopData(
    @PrimaryKey
    var title: String = "",
    var url: String = "",
    var variables: RealmList<Variable> = RealmList()
) : RealmObject()

open class Variable(
    var key: String = "",
    var variable: String = ""
) : RealmObject()
