package com.vastausf.wesolient.model.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Template
import javax.inject.Inject

class TemplateStore
@Inject
constructor(
    private val firebaseDatabaseUserRoot: DatabaseReference
) {
    private val templatesPath = "TEMPLATES"

    private fun firebaseDatabaseTemplates(scopeUid: String) =
        firebaseDatabaseUserRoot
            .child(scopeUid)
            .child(templatesPath)

    fun createTemplate(
        scopeUid: String,
        title: String,
        message: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        val newTemplate = Template(
            title = title,
            message = message
        )

        firebaseDatabaseTemplates(scopeUid)
            .child(newTemplate.uid)
            .setValue(newTemplate)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun editTemplate(
        scopeUid: String,
        uid: String,
        newTitle: String,
        newMessage: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseTemplates(scopeUid)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue<Template>()?.let { template ->
                        firebaseDatabaseTemplates(scopeUid)
                            .child(uid)
                            .setValue(template.apply {
                                title = newTitle
                                message = newMessage
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

    fun deleteTemplate(
        scopeUid: String,
        uid: String,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseTemplates(scopeUid)
            .child(uid)
            .removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getTemplateOnce(
        scopeUid: String,
        uid: String,
        onFailure: () -> Unit = {},
        onNotFound: () -> Unit = {},
        onSuccess: (Template) -> Unit = {}
    ) {
        firebaseDatabaseTemplates(scopeUid)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Template>()

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

    fun onTemplateListUpdate(
        scopeUid: String,
        onFailure: () -> Unit = {},
        onUpdate: (List<Template>) -> Unit = {}
    ) {
        firebaseDatabaseTemplates(scopeUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value =
                        snapshot.getValue<HashMap<String, Template>>()?.values?.toList()
                            ?: emptyList()

                    onUpdate(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }
}