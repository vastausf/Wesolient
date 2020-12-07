package com.vastausf.wesolient.model.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Variable
import javax.inject.Inject

class VariableStore
@Inject
constructor(
    private val firebaseDatabaseUserRoot: DatabaseReference
) {
    private val variablesPath = "VARIABLES"

    private fun firebaseDatabaseVariables(scopeUid: String) =
        firebaseDatabaseUserRoot
            .child(scopeUid)
            .child(variablesPath)

    fun createVariable(
        scopeUid: String,
        title: String,
        value: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        val newVariable = Variable(
            title = title,
            value = value
        )

        firebaseDatabaseVariables(scopeUid)
            .child(newVariable.uid)
            .setValue(newVariable)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun editVariable(
        scopeUid: String,
        uid: String,
        newTitle: String,
        newValue: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue<Variable>()?.let { variable ->
                        firebaseDatabaseVariables(scopeUid)
                            .child(uid)
                            .setValue(variable.apply {
                                title = newTitle
                                value = newValue
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

    fun deleteVariable(
        scopeUid: String,
        uid: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getVariableOnce(
        scopeUid: String,
        uid: String,
        onFailure: () -> Unit = {},
        onNotFound: () -> Unit = {},
        onSuccess: (Variable) -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Variable>()

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

    fun onVariableUpdate(
        scopeUid: String,
        uid: String,
        onFailure: () -> Unit = {},
        onNotFound: () -> Unit = {},
        onUpdate: (Variable) -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Variable>()

                    if (value != null) {
                        onUpdate(value)
                    } else {
                        onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }

    fun getVariableListOnce(
        scopeUid: String,
        onFailure: () -> Unit = {},
        onSuccess: (List<Variable>) -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Variable>>()?.values?.toList()
                            ?: emptyList()

                    onSuccess(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }

    fun onVariableListUpdate(
        scopeUid: String,
        onFailure: () -> Unit = {},
        onUpdate: (List<Variable>) -> Unit = {}
    ) {
        firebaseDatabaseVariables(scopeUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Variable>>()?.values?.toList()
                            ?: emptyList()

                    onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }
}