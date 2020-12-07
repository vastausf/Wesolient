package com.vastausf.wesolient.model.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Scope
import javax.inject.Inject

class ScopeStore
@Inject
constructor(
    private val firebaseDatabaseUserRoot: DatabaseReference
) {
    private val scopesPath = "SCOPES"

    private fun firebaseDatabaseScopes() =
        firebaseDatabaseUserRoot
            .child(scopesPath)

    fun createScope(
        title: String,
        url: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        val newScope = Scope(
            title = title,
            url = url
        )

        firebaseDatabaseScopes()
            .child(newScope.uid)
            .setValue(newScope)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun editScope(
        uid: String,
        newTitle: String,
        newUrl: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseScopes()
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue<Scope>()?.let { scope ->
                        firebaseDatabaseScopes()
                            .child(uid)
                            .setValue(scope.apply {
                                title = newTitle
                                url = newUrl
                            })
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener {
                                onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }

    fun deleteScope(
        uid: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseScopes()
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getScopeOnce(
        uid: String,
        onFailure: () -> Unit = {},
        onNotFound: () -> Unit = {},
        onSuccess: (Scope) -> Unit = {}
    ) {
        firebaseDatabaseScopes()
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        onSuccess(value)
                    } else {
                        onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }

    fun onScopeListUpdate(
        onFailure: () -> Unit = {},
        onUpdate: (List<Scope>) -> Unit = {}
    ) {
        firebaseDatabaseScopes()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Scope>>()?.values?.toList() ?: emptyList()

                    onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }
}
