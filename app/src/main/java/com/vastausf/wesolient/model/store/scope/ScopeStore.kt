package com.vastausf.wesolient.model.store.scope

import com.vastausf.wesolient.data.common.Scope
import kotlinx.coroutines.flow.StateFlow

interface ScopeStore {
    fun createScope(title: String, url: String)

    fun editScope(uid: String, block: Scope?.() -> Unit)

    fun deleteScope(uid: String)

    fun getScope(uid: String): StateFlow<Scope?>

    fun getScopeList(): StateFlow<List<Scope>?>
}
