package com.vastausf.wesolient.model

import com.vastausf.wesolient.model.data.DesktopData
import io.realm.Realm
import javax.inject.Inject

class RealmDesktopDataStore
@Inject
constructor(
       private val realm: Realm
) : DesktopDataStore{
    override fun create(title: String, url: String): DesktopData {
        return realm.copyToRealm(DesktopData(
            title = title,
            url = url
        ))
    }
}
