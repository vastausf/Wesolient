package com.vastausf.wesolient.model

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion

inline fun <reified T : RealmObject> Realm.getAndSubscribe(
    maxDepth: Int = 1,
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<T?> {
    val currentValue = block().findFirst()

    val mutableStateFlow = MutableStateFlow(
        if (currentValue != null)
            copyFromRealm(currentValue, maxDepth)
        else
            null
    )

    val listener = RealmChangeListener<Realm> {
        val value = it.block().findFirst()
        mutableStateFlow.value = if (value != null) it.copyFromRealm(value, maxDepth) else null
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        removeChangeListener(listener)
    }

    return mutableStateFlow
}

inline fun <reified T : RealmObject> Realm.getOrCreateAndSubscribe(
    maxDepth: Int = 1,
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<T> {
    val currentValue = block().findFirst() ?: createObject()

    val mutableStateFlow = MutableStateFlow(copyFromRealm(currentValue, maxDepth))

    val listener = RealmChangeListener<Realm> {
        val value = it.block().findFirst()
        mutableStateFlow.value = it.copyFromRealm(value, maxDepth)
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        removeChangeListener(listener)
    }

    return mutableStateFlow
}

inline fun <reified T : RealmObject> Realm.getListAndSubscribe(
    maxDepth: Int = 1,
    crossinline block: Realm.() -> RealmQuery<T>
): MutableStateFlow<List<T>?> {
    val currentValue = block().findAll()

    val mutableStateFlow = MutableStateFlow(copyFromRealm(currentValue, maxDepth))

    val listener = RealmChangeListener<Realm> {
        val value = it.block().findAll()
        mutableStateFlow.value = it.copyFromRealm(value, maxDepth)
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
