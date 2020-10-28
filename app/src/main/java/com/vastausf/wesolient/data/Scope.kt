package com.vastausf.wesolient.data

import com.vastausf.wesolient.model.data.ScopeRealm

data class Scope(
    val title: String,
    val url: String
) {
    companion object {
        fun fromRealm(scopeRealm: ScopeRealm): Scope {
            return Scope(
                scopeRealm.title,
                scopeRealm.url
            )
        }
    }
}
