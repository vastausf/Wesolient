package com.vastausf.wesolient.model

import com.vastausf.wesolient.model.data.ScopeData
import io.realm.Realm
import javax.inject.Inject

class RealmDesktopDataStore
@Inject
constructor(
       private val realm: Realm
) : DesktopDataStore{
    override fun create(title: String, url: String): ScopeData {
        return realm.copyToRealm(ScopeData(
            title = title,
            url = url
        ))
    }
}
