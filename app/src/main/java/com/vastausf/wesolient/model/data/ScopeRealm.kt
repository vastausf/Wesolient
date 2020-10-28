package com.vastausf.wesolient.model.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ScopeRealm(
    @PrimaryKey
    var title: String = "",
    var url: String = ""
) : RealmObject()
