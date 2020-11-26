package com.vastausf.wesolient.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
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

    fun createScope(title: String, url: String, createListener: CreateListener? = null) {
        val newScope = Scope(
            title = title,
            url = url
        )

        firebaseDatabaseScopes
            .child(newScope.uid)
            .setValue(newScope)
            .addOnSuccessListener {
                createListener?.onSuccess()
            }
            .addOnFailureListener {
                createListener?.onFailure()
            }
    }

    fun editScope(
        uid: String,
        newTitle: String,
        newUrl: String,
        createListener: CreateListener? = null
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
                                createListener?.onSuccess()
                            }
                            .addOnFailureListener {
                                createListener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    createListener?.onFailure()
                }
            })
    }

    fun deleteScope(uid: String, deleteListener: DeleteListener? = null) {
        firebaseDatabaseScopes
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                deleteListener?.onSuccess()
            }
            .addOnFailureListener {
                deleteListener?.onFailure()
            }
    }

    fun getScopeOnce(uid: String, valueListener: ValueListener<Scope>? = null) {
        firebaseDatabaseScopes
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        valueListener?.onSuccess(value)
                    } else {
                        valueListener?.onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    valueListener?.onFailure()
                }
            })
    }

    fun onScopeUpdate(uid: String, updateListener: UpdateListener<Scope>? = null) {
        firebaseDatabaseScopes
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        updateListener?.onUpdate(value)
                    } else {
                        updateListener?.onFailure()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    updateListener?.onFailure()
                }
            })
    }

    fun onScopeListUpdate(updateListener: UpdateListener<List<Scope>>? = null) {
        firebaseDatabaseScopes
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Scope>>()?.values?.toList() ?: emptyList()

                    updateListener?.onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    updateListener?.onFailure()
                }
            })
    }


    fun createTemplate(
        scopeUid: String,
        title: String,
        message: String,
        createListener: CreateListener? = null
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
                createListener?.onSuccess()
            }
            .addOnFailureListener {
                createListener?.onFailure()
            }
    }

    fun editTemplate(
        scopeUid: String,
        uid: String,
        newTitle: String,
        newMessage: String,
        createListener: CreateListener? = null
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
                            .child(uid)
                            .setValue(value.apply {
                                title = newTitle
                                message = newMessage
                            })
                            .addOnSuccessListener {
                                createListener?.onSuccess()
                            }
                            .addOnFailureListener {
                                createListener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    createListener?.onFailure()
                }
            })
    }

    fun deleteTemplate(scopeUid: String, uid: String, deleteListener: DeleteListener? = null) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                deleteListener?.onSuccess()
            }
            .addOnFailureListener {
                deleteListener?.onFailure()
            }
    }

    fun getTemplateOnce(
        scopeUid: String,
        uid: String,
        valueListener: ValueListener<Template>? = null
    ) {
        firebaseDatabaseScopes
            .child(scopeUid)
            .child(templatesPath)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Template>()

                    if (value != null) {
                        valueListener?.onSuccess(value)
                    } else {
                        valueListener?.onNotFound()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    valueListener?.onFailure()
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
}
