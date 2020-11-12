package com.vastausf.wesolient.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.vastausf.wesolient.model.data.Message
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.ChangeListener
import javax.inject.Inject

class ScopeStore
@Inject
constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseUser: FirebaseUser
) {
    private val changeListeners: MutableList<ChangeListener> = mutableListOf()

    private val usersTable = "USERS"

    fun create(title: String, url: String): Scope {
        val newScope = Scope(
            title = title,
            url = url
        )

        firebaseDatabase
            .reference
            .child(usersTable)
            .child(firebaseUser.uid)
            .child(newScope.uid)
            .setValue(newScope)

        return newScope
    }

    fun edit(id: String, newTitle: String, newUrl: String) {
        firebaseDatabase
            .reference
            .child(usersTable)
            .child(firebaseUser.uid)
    }

    fun getAll(): List<Scope> {
        return emptyList()
    }

    fun getById(id: String): Scope? {
        return null
    }

    fun addMessageInScopeHistory(id: String, message: Message) {

    }

    fun registerListener(listener: ChangeListener) {
        changeListeners.add(listener)
    }

    fun unregisterListener(listener: ChangeListener) {
        changeListeners.remove(listener)
    }
}
