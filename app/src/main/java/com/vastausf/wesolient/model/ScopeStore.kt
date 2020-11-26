package com.vastausf.wesolient.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.data.common.Variable
import com.vastausf.wesolient.model.listener.CreateListener
import com.vastausf.wesolient.model.listener.DeleteListener
import com.vastausf.wesolient.model.listener.UpdateListener
import com.vastausf.wesolient.model.listener.ValueListener
import javax.inject.Inject

class ScopeStore
@Inject
constructor(
    private val firebaseDatabaseScopes: DatabaseReference
) {
    private val templatesPath = "TEMPLATES"
    private val variablesPath = "VARIABLES"

    fun createScope(
        title: String,
        url: String,
        listener: CreateListener? = null
    ) {
        val newScope = Scope(
            title = title,
            url = url
        )

        firebaseDatabaseScopes
            .child(newScope.uid)
            .setValue(newScope)
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun editScope(
        uid: String,
        newTitle: String,
        newUrl: String,
        listener: CreateListener? = null
    ) {
        firebaseDatabaseScopes
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        firebaseDatabaseScopes
                            .child(uid)
                            .setValue(value.apply {
                                title = newTitle
                                url = newUrl
                            })
                            .addOnSuccessListener {
                                listener?.onSuccess()
                            }
                            .addOnFailureListener {
                                listener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun deleteScope(
        uid: String,
        listener: DeleteListener? = null
    ) {
        firebaseDatabaseScopes
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun getScopeOnce(
        uid: String,
        listener: ValueListener<Scope>? = null
    ) {
        firebaseDatabaseScopes
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        listener?.onSuccess(value)
                    } else {
                        listener?.onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun onScopeListUpdate(
        listener: UpdateListener<List<Scope>>? = null
    ) {
        firebaseDatabaseScopes
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Scope>>()?.values?.toList() ?: emptyList()

                    listener?.onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }


    fun createTemplate(
        scopeUid: String,
        title: String,
        message: String,
        listener: CreateListener? = null
    ) {
        val newTemplate = Template(
            title = title,
            message = message
        )

        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(newTemplate.uid)
            .setValue(newTemplate)
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun editTemplate(
        scopeUid: String,
        uid: String,
        newTitle: String,
        newMessage: String,
        listener: CreateListener? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Template>()

                    if (value != null) {
                        firebaseDatabaseScopes
                            .child(scopeUid)
                            .child(templatesPath)
                            .child(uid)
                            .setValue(value.apply {
                                title = newTitle
                                message = newMessage
                            })
                            .addOnSuccessListener {
                                listener?.onSuccess()
                            }
                            .addOnFailureListener {
                                listener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun deleteTemplate(
        scopeUid: String,
        uid: String,
        listener: DeleteListener? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun getTemplateOnce(
        scopeUid: String,
        uid: String,
        listener: ValueListener<Template>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Template>()

                    if (value != null) {
                        listener?.onSuccess(value)
                    } else {
                        listener?.onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun onTemplateListUpdate(
        scopeUid: String,
        updateListener: UpdateListener<List<Template>>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Template>>()?.values?.toList()
                            ?: emptyList()

                    updateListener?.onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    updateListener?.onFailure()
                }
            })
    }


    fun createVariable(
        scopeUid: String,
        title: String,
        value: String,
        listener: CreateListener? = null
    ) {
        val newVariable = Variable(
            title = title,
            value = value
        )

        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .child(newVariable.uid)
            .setValue(newVariable)
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun editVariable(
        scopeUid: String,
        uid: String,
        newTitle: String,
        newValue: String,
        listener: CreateListener? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val variable = snapshot.getValue<Variable>()

                    if (variable != null) {
                        firebaseDatabaseScopes
                            .child(scopeUid)
                            .child(variablesPath)
                            .child(uid)
                            .setValue(variable.apply {
                                title = newTitle
                                value = newValue
                            })
                            .addOnSuccessListener {
                                listener?.onSuccess()
                            }
                            .addOnFailureListener {
                                listener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun deleteVariable(
        scopeUid: String,
        uid: String,
        listener: DeleteListener? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                listener?.onSuccess()
            }
            .addOnFailureListener {
                listener?.onFailure()
            }
    }

    fun getVariableOnce(
        scopeUid: String,
        uid: String,
        listener: ValueListener<Variable>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Variable>()

                    if (value != null) {
                        listener?.onSuccess(value)
                    } else {
                        listener?.onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun onVariableUpdate(
        scopeUid: String,
        uid: String,
        listener: UpdateListener<Variable>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Variable>()

                    if (value != null) {
                        listener?.onUpdate(value)
                    } else {
                        listener?.onFailure()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun getVariableListOnce(
        scopeUid: String,
        listener: ValueListener<List<Variable>>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Variable>>()?.values?.toList()
                            ?: emptyList()

                    listener?.onSuccess(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    listener?.onFailure()
                }
            })
    }

    fun onVariableListUpdate(
        scopeUid: String,
        updateListener: UpdateListener<List<Variable>>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(variablesPath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Variable>>()?.values?.toList()
                            ?: emptyList()

                    updateListener?.onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    updateListener?.onFailure()
                }
            })
    }
}
