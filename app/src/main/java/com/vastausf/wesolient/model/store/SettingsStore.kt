package com.vastausf.wesolient.model.store

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.vastausf.wesolient.data.common.Settings
import javax.inject.Inject

class SettingsStore
@Inject
constructor(
    private val firebaseDatabaseUserRoot: DatabaseReference
) {
    private val settingsPath = "SETTINGS"

    private fun firebaseDatabaseSettings() =
        firebaseDatabaseUserRoot
            .child(settingsPath)

    fun createSettings(
        settings: Settings,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseSettings()
            .setValue(settings)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun editSettings(
        settings: Settings,
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseSettings()
            .setValue(settings)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun deleteSettings(
        onFailure: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        firebaseDatabaseSettings()
            .removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun getSettingsOnce(
        onFailure: () -> Unit = {},
        onSuccess: (Settings) -> Unit = {}
    ) {
        firebaseDatabaseSettings()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Settings>()

                    onSuccess(value ?: Settings())
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }

    fun onSettingsUpdate(
        onFailure: () -> Unit = {},
        onUpdate: (Settings) -> Unit = {}
    ) {
        firebaseDatabaseSettings()
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Settings>()

                    onUpdate(value ?: Settings())
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure()
                }
            })
    }
}