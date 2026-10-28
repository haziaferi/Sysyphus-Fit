package com.hazia.sisyphusfit.data

import kotlinx.datetime.TimeZone
import kotlin.time.Clock

/**
 * The database as the stores reach it: opened on first use, so the app can open it lazily and off the
 * main thread (`openDatabase` blocks while it probes), and a test can hand in its own.
 */
typealias Db = suspend () -> SisyphusDatabase

/** The time and the zone a row is recorded in (SPEC §4: epoch milliseconds UTC plus the zone). */
class WallClock(val now: () -> Long, val zone: () -> String) {
    companion object {
        val System = WallClock({ Clock.System.now().toEpochMilliseconds() }, { TimeZone.currentSystemDefault().id })
    }
}
