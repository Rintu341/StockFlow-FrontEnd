package org.sujan.stockflow.presentation.util

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun nowLocal(): LocalDateTime =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun Instant.toDeviceLocalDate(): LocalDate =
    toLocalDateTime(TimeZone.currentSystemDefault()).date

/** "SUN, OCT 4" */
fun formatHeaderDate(date: LocalDate): String =
    "${date.dayOfWeek.name.take(3)}, ${date.month.name.take(3)} ${date.day}"

/** "Oct 4" */
fun formatOrderDate(date: LocalDate): String {
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${date.day}"
}

fun greetingFor(hour: Int): String = when {
    hour < 12 -> "Good morning"
    hour < 17 -> "Good afternoon"
    else -> "Good evening"
}