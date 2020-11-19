package com.vastausf.wesolient.model.listener

interface ValueListener<T> {
    fun onSuccess(value: T) {}

    fun onNotFound() {}

    fun onFailure() {}
}
