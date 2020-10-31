package com.vastausf.wesolient.model.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ScopeRealm(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var url: String = "",
    var history: RealmList<MessageRealm> = RealmList()
) : RealmObject()