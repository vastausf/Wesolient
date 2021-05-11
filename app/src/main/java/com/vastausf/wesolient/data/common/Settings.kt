package com.vastausf.wesolient.data.common

import io.realm.RealmObject

open class Settings : RealmObject() {
    var autoConnect: Boolean = false

    var retryOnConnectionFailure: Boolean = true
}
