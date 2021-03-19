package com.vastausf.wesolient.model.store.settings

import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.getAndSubscribe
import io.realm.Realm
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsStoreRealm
@Inject
constructor(
    private val realm: Realm
) : SettingsStore {
    override fun getSettings(): MutableStateFlow<Settings> {
        return realm.getAndSubscribe {
            where(Settings::class.java).findFirst() ?: Settings()
        }
    }

    override fun updateSettings(block: Settings.() -> Unit) {
        realm.executeTransactionAsync {
            val settings = it.where(Settings::class.java).findFirst() ?: Settings()

            settings.block()

            it.insertOrUpdate(settings)
        }
    }
}