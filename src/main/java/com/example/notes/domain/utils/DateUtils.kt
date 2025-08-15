package com.example.notes.domain.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
object DateUtils {

    fun durationToText(noteTime: Instant): String {
        val nowInstant = Clock.System.now()
        val timeDistance = nowInstant - noteTime

        val noteDate = noteTime.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return when {
            timeDistance < 1.minutes -> "Just now"
            timeDistance < 1.hours -> "${timeDistance.inWholeMinutes}m ago"
            timeDistance < 1.days -> "${timeDistance.inWholeHours}h ago"
            timeDistance < 2.days -> "Yesterday"
            timeDistance < 7.days -> "${timeDistance.inWholeDays}d ago"
            timeDistance < 365.days -> "${noteDate.month.name.take(3)} ${noteDate.day}"
            else -> "${noteDate.month.name.take(3)} ${noteDate.day}, ${noteDate.year}"
        }
    }
}