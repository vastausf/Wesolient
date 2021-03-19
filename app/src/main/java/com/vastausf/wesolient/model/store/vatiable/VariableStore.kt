package com.vastausf.wesolient.model.store.vatiable

import com.vastausf.wesolient.data.common.Variable

interface VariableStore {
    suspend fun createVariable(scopeUid: String, title: String, value: String)

    suspend fun editVariable(scopeUid: String, uid: String, block: Variable.() -> Unit)

    suspend fun deleteVariable(scopeUid: String, uid: String)

    suspend fun getVariable(scopeUid: String, uid: String)

    suspend fun getVariableList(scopeUid: String)
}
