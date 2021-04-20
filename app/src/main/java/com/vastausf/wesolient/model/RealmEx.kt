package com.vastausf.wesolient.model

import android.util.Log
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion

inline fun <reified T : RealmObject> Realm.getAndSubscribe(
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<T?> {
    val mutableStateFlow = MutableStateFlow<T?>(null)

    block().findFirst()?.let {
        mutableStateFlow.value = copyFromRealm(it)
    }

    val listener = RealmChangeListener<Realm> {
        val value = it.block().findFirst()
        mutableStateFlow.value = if (value != null) it.copyFromRealm(value) else null
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        removeChangeListener(listener)
    }

    return mutableStateFlow
}

inline fun <reified T : RealmObject> Realm.getOrCreateAndSubscribe(
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<T?> {
    val mutableStateFlow = MutableStateFlow<T?>(null)

    val initValue = block().findFirst()

    if (initValue == null) {
        executeTransactionAsync {
            it.createObject<T>()
        }
    } else {
        mutableStateFlow.value = initValue
    }

    val listener = RealmChangeListener<Realm> { listenerRealm ->
        mutableStateFlow.value = listenerRealm
            .copyFromRealm(listenerRealm.block().findFirst())
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        Log.d("ss", "ss")
        removeChangeListener(listener)
    }

    return mutableStateFlow
}

inline fun <reified T : RealmObject> Realm.getListAndSubscribe(
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<List<T>?> {
    val mutableStateFlow = MutableStateFlow<List<T>?>(null)

    block().findAll()?.let {
        mutableStateFlow.value = copyFromRealm(it)
    }

    val listener = RealmChangeListener<Realm> { realm ->
        val value = realm.block().findAll()
        mutableStateFlow.value = realm.copyFromRealm(value)
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        removeChangeListener(listener)
    }

    return mutableStateFlow
}

inline fun <reified T : RealmObject> Realm.findAndApply(
    crossinline item: RealmQuery<T>.() -> RealmQuery<T>,
    crossinline block: T.() -> Unit
) {
    executeTransactionAsync {
        val value = it.where<T>().item().findFirst() ?: return@executeTransactionAsync

        value.block()

        it.insertOrUpdate(value)
    }
}

inline fun <reified T : RealmObject> Realm.findOrCreateAndApply(
    crossinline item: RealmQuery<T>.() -> RealmQuery<T> = { this },
    crossinline block: T.() -> Unit
) {
    executeTransactionAsync {
        val value = it.where<T>().item().findFirst() ?: it.createObject()

        value.block()

        it.insertOrUpdate(value)
    }
}
