package com.vastausf.wesolient

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
