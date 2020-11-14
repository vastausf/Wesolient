package com.vastausf.wesolient.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.model.data.Message
import com.vastausf.wesolient.model.data.Scope
import com.vastausf.wesolient.model.listener.DeleteListener
import com.vastausf.wesolient.model.listener.UpdateListener
import com.vastausf.wesolient.model.listener.ValueListener
import com.vastausf.wesolient.model.listener.WriteListener
import javax.inject.Inject

class ScopeStore
@Inject
constructor(
    private val firebaseDatabaseScopes: DatabaseReference
) {
    fun createScope(title: String, url: String, writeListener: WriteListener? = null) {
        val newScope = Scope(
            title = title,
            url = url
        )

        firebaseDatabaseScopes
            .child(newScope.uid)
            .setValue(newScope)
            .addOnSuccessListener {
                writeListener?.onSuccess()
            }
            .addOnFailureListener {
                writeListener?.onFailure()
            }
    }

    fun editScope(
        uid: String,
        newTitle: String,
        newUrl: String,
        writeListener: WriteListener? = null
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
                                writeListener?.onSuccess()
                            }
                            .addOnFailureListener {
                                writeListener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    writeListener?.onFailure()
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

    fun onUpdate(updateListener: UpdateListener<List<Scope>>? = null) {
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

    fun getScope(id: String, updateListener: UpdateListener<Scope>? = null) {
        firebaseDatabaseScopes
            .child(id)
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

    fun getScopeOnce(id: String, valueListener: ValueListener<Scope>? = null) {
        firebaseDatabaseScopes
            .child(id)
            .addValueEventListener(object : ValueEventListener {
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

    fun addMessageInScopeHistory(
        id: String,
        message: Message,
        writeListener: WriteListener? = null
    ) {
        firebaseDatabaseScopes
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Scope>()

                    if (value != null) {
                        firebaseDatabaseScopes
                            .child(id)
                            .setValue(value.history.apply { add(message) })
                            .addOnSuccessListener {
                                writeListener?.onSuccess()
                            }
                            .addOnFailureListener {
                                writeListener?.onFailure()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    writeListener?.onFailure()
                }
            })
    }
}
