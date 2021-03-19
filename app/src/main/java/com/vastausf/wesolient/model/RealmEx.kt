package com.vastausf.wesolient.model

import io.realm.Realm
import io.realm.RealmChangeListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion

inline fun <reified T> Realm.getAndSubscribe(
    crossinline block: Realm.() -> T
): MutableStateFlow<T> {
    val mutableStateFlow = MutableStateFlow(block())

    val listener = RealmChangeListener<Realm> {
        mutableStateFlow.value = it.block()
    }

    addChangeListener(listener)

    mutableStateFlow.onCompletion {
        removeChangeListener(listener)
    }

    return mutableStateFlow
}
