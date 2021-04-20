package com.vastausf.wesolient.model.store.settings

import com.vastausf.wesolient.data.common.Settings
import com.vastausf.wesolient.model.findOrCreateAndApply
import com.vastausf.wesolient.model.getOrCreateAndSubscribe
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsStoreRealm
@Inject
constructor(
    private val realm: Realm
) : SettingsStore {
    override fun getSettings(): MutableStateFlow<Settings?> {
        return realm.getOrCreateAndSubscribe {
            where(Settings::class.java)
        }
    }

    override fun updateSettings(block: Settings.() -> Unit) {
        realm.findOrCreateAndApply(
            block = block
        )
    }
}