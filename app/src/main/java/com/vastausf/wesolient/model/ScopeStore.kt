package com.vastausf.wesolient.model

import com.vastausf.wesolient.data.Message
import com.vastausf.wesolient.data.Scope
import com.vastausf.wesolient.model.data.MessageRealm
import com.vastausf.wesolient.model.data.ScopeRealm
import com.vastausf.wesolient.model.listener.ChangeListener
import io.realm.Realm
import javax.inject.Inject

class ScopeStore
@Inject
constructor(
    private val realm: Realm
) {
    private val changeListeners: MutableList<ChangeListener> = mutableListOf()

    init {
        realm.addChangeListener {
            changeListeners.forEach { it.onChange() }
        }
    }

    fun create(title: String, url: String): Scope {
        val newScope = ScopeRealm(
            title = title,
            url = url
        )

        realm.executeTransaction {
            realm.copyToRealm(newScope)
        }

        return Scope.fromRealm(newScope)
    }

    fun edit(id: String, newTitle: String, newUrl: String) {
        val scopeRealm = realm
            .where(ScopeRealm::class.java)
            .equalTo("id", id)
            .findFirst()

        realm.executeTransaction {
            scopeRealm?.apply {
                title = newTitle
                url = newUrl
            }
        }
    }

    fun getAll(): List<Scope> {
        return realm
            .where(ScopeRealm::class.java)
            .findAll()
            .map {
                Scope.fromRealm(it)
            }
    }

    fun getById(id: String): Scope? {
        return realm
            .where(ScopeRealm::class.java)
            .and()
            .equalTo("id", id)
            .findFirst()
            ?.let {
                Scope.fromRealm(it)
            }
    }

    fun addMessageInScopeHistory(id: String, message: Message): Boolean {
        val scope = realm
            .where(ScopeRealm::class.java)
            .and()
            .equalTo("id", id)
            .findFirst()

        return if (scope != null) {
            realm.executeTransaction {
                scope
                    .history
                    .add(message.let {
                        MessageRealm(
                            it.id,
                            it.source,
                            it.content,
                            it.dateTime
                        )
                    })
            }

            true
        } else false
    }

    fun registerListener(listener: ChangeListener) {
        changeListeners.add(listener)
    }

    fun unregisterListener(listener: ChangeListener) {
        changeListeners.remove(listener)
    }
}
