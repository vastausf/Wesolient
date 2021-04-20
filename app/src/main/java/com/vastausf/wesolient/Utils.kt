package com.vastausf.wesolient

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.vastausf.wesolient.data.common.Template
import com.vastausf.wesolient.data.common.Variable
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun getLocalSystemTimestamp(): Long {
    return LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()
}

fun getLocalStringFromTimestamp(pattern: String, timestamp: Long): String {
    return DateTimeFormatter.ofPattern(pattern)
        .format(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC))
}

fun NavController.sendDialogResult(key: String, result: Any) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.set(
            key,
            result
        )
}

fun NavController.sendResult(key: String, result: Any) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(
            key,
            result
        )
}

fun <T> NavController.listenResult(key: String, lifecycleOwner: LifecycleOwner, body: (T) -> Unit) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.observe(lifecycleOwner) {
            body(it)
        }
}

fun Template.replaceVariables(variables: List<Variable>): String {
    return message.replaceVariables(variables)
}

fun String.replaceVariables(variables: List<Variable>): String {
    var replacedMessage = this

    variables.forEach {
        replacedMessage = replacedMessage.replace("@{${it.title}}", it.value)
    }

    return replacedMessage
}

fun <T> StateFlow<SingleEvent<T>?>.filterHandled(): Flow<T> = transform { value ->
    val unhandledValue = value?.getValueIfNotHandled()

    if (unhandledValue != null) return@transform emit(unhandledValue)
}

class SingleEvent<out T>(val value: T) {
    var hasBeenHandled: Boolean = false

    fun getValueIfNotHandled(): T? =
        if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            value
        }
}
