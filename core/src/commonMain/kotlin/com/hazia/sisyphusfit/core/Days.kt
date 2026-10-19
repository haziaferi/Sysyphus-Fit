package com.hazia.sisyphusfit.core

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * The local date a session counts on: the day it STARTED, in the zone it was recorded in
 * (harvest row FA13, Factotum T3c.2). Stored with the session, so a later change of the phone's
 * zone does not move history.
 */
fun countingDay(startedAtMillis: Long, zoneId: String): LocalDate =
    Instant.fromEpochMilliseconds(startedAtMillis).toLocalDateTime(TimeZone.of(zoneId)).date

/** A timed span in epoch milliseconds, end exclusive. */
data class Span(val start: Long, val end: Long) {
    init {
        require(end >= start) { "a span ends before it starts: $start..$end" }
    }
}

/**
 * A day's training time in seconds: the time its timed spans cover, counted once where they
 * overlap, plus its untimed amounts (harvest rows FA10, FA11; Factotum decision 07 extended).
 * Two timers running at once (a session clock and an interval set) are not double-counted.
 */
fun dayTotalSeconds(spans: List<Span>, amountsSeconds: List<Long>): Long {
    require(amountsSeconds.all { it >= 0 }) { "a negative amount" }
    var covered = 0L
    var curStart = Long.MIN_VALUE
    var curEnd = Long.MIN_VALUE
    for (s in spans.sortedBy { it.start }) {
        if (s.start > curEnd) {
            if (curEnd > curStart) covered += curEnd - curStart
            curStart = s.start
            curEnd = s.end
        } else if (s.end > curEnd) {
            curEnd = s.end
        }
    }
    if (curEnd > curStart) covered += curEnd - curStart
    return covered / 1000 + amountsSeconds.sum()
}
