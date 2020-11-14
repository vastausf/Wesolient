package com.vastausf.wesolient.model.listener

interface UpdateListener<T> {
    fun onUpdate(snapshot: T) {}

    fun onFailure() {}
}
