package com.vastausf.wesolient

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.vastausf.wesolient.data.common.Scope
import com.vastausf.wesolient.data.common.Template
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

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

fun Template.replaceVariables(scope: Scope): String {
    var replacedMessage = message

    scope.variables.forEach {
        replacedMessage = replacedMessage.replace("@{${it.title}}", it.value)
    }

    return replacedMessage
}
