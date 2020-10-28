package com.vastausf.wesolient.model

import com.vastausf.wesolient.data.Scope
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

    fun create(title: String): Scope {
        val newScope = ScopeRealm(
            title = title
        )

        realm.executeTransaction {
            realm.copyToRealm(newScope)
        }

        return Scope.fromRealm(newScope)
    }

    fun getAll(): List<Scope> {
        return realm
            .where(ScopeRealm::class.java)
            .findAll()
            .map {
                Scope.fromRealm(it)
            }
    }

    fun registerListener(listener: ChangeListener) {
        changeListeners.add(listener)
    }

    fun unregisterListener(listener: ChangeListener) {
        changeListeners.remove(listener)
    }
}
