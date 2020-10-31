package com.vastausf.wesolient.data

import com.vastausf.wesolient.model.data.ScopeRealm

data class Scope(
    val id: String,
    val title: String,
    val url: String,
    val history: List<Message>
) {
    companion object {
        fun fromRealm(scopeRealm: ScopeRealm): Scope {
            return Scope(
                scopeRealm.id,
                scopeRealm.title,
                scopeRealm.url,
                scopeRealm.history.map {
                    Message(
                        it.id,
                        it.source,
                        it.content,
                        it.dateTime
                    )
                }
            )
        }
    }
}
