package com.vastausf.wesolient.model.store.vatiable

import com.vastausf.wesolient.data.common.Variable
import io.realm.Realm
import javax.inject.Inject

class VariableStoreRealm
@Inject
constructor(
    private val realm: Realm
) : VariableStore {
    override suspend fun createVariable(scopeUid: String, title: String, value: String) {

    }

    override suspend fun editVariable(scopeUid: String, uid: String, block: Variable.() -> Unit) {

    }

    override suspend fun deleteVariable(scopeUid: String, uid: String) {

    }

    override suspend fun getVariable(scopeUid: String, uid: String) {

    }

    override suspend fun getVariableList(scopeUid: String) {

    }
}