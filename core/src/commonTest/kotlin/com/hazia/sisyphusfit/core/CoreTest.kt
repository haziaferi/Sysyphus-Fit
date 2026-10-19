package com.hazia.sisyphusfit.core

import kotlinx.datetime.LocalDate
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class IdsTest {
    @Test
    fun aUlidIs26CharsAndCarriesItsTime() {
        val id = Ids.ulid(1_760_000_000_000, Random(1))
        assertEquals(26, id.length)
        assertEquals(1_760_000_000_000, Ids.timeOf(id))
    }

    @Test
    fun ulidsSortByTime() {
        val a = Ids.ulid(1_000, Random(1))
        val b = Ids.ulid(2_000, Random(2))
        assertTrue(a < b)
    }

    @Test
    fun aThousandUlidsInOneMillisecondAreDistinct() {
        val r = Random(7)
        assertEquals(1000, List(1000) { Ids.ulid(5, r) }.toSet().size)
    }
}

class DaysTest {
    // 2026-10-19 21:30 UTC: 23:30 in Rome (summer time until 25 October), 06:30 next day in Tokyo.
    private val lateInRome = 1_792_445_400_000L

    @Test
    fun aSessionCountsOnTheDayItStartsInItsOwnZone() {
        assertEquals(LocalDate(2026, 10, 19), countingDay(lateInRome, "Europe/Rome"))
        assertEquals(LocalDate(2026, 10, 20), countingDay(lateInRome, "Asia/Tokyo"))
    }

    @Test
    fun overlappingSpansCountOnceAndAmountsAdd() {
        val spans = listOf(Span(0, 60_000), Span(30_000, 90_000), Span(120_000, 180_000))
        assertEquals(90 + 60 + 600, dayTotalSeconds(spans, listOf(600)))
    }

    @Test
    fun aSpanInsideAnotherAddsNothing() {
        assertEquals(60, dayTotalSeconds(listOf(Span(0, 60_000), Span(10_000, 20_000)), emptyList()))
    }

    @Test
    fun emptyDayIsZero() = assertEquals(0, dayTotalSeconds(emptyList(), emptyList()))

    @Test
    fun aBackwardsSpanIsRefused() {
        assertFailsWith<IllegalArgumentException> { Span(10, 5) }
    }
}

class ProgressionTest {
    private val t = Target(sets = 3, reps = 5, weightGrams = 60_000)
    private val rule = Rule(addWeightGrams = 2_500, addReps = 0)

    @Test
    fun everySetHitAdvances() {
        val done = List(3) { DoneSet(5, 60_000) }
        assertEquals(t.copy(weightGrams = 62_500), nextTarget(t, done, rule))
    }

    @Test
    fun oneMissedSetHolds() {
        val done = listOf(DoneSet(5, 60_000), DoneSet(4, 60_000), DoneSet(5, 60_000))
        assertEquals(t, nextTarget(t, done, rule))
    }

    @Test
    fun tooFewSetsHolds() = assertEquals(t, nextTarget(t, List(2) { DoneSet(5, 60_000) }, rule))

    @Test
    fun aLighterSetHolds() {
        val done = listOf(DoneSet(5, 60_000), DoneSet(5, 57_500), DoneSet(5, 60_000))
        assertEquals(t, nextTarget(t, done, rule))
    }

    @Test
    fun aRuleNeverLowersATarget() {
        assertFailsWith<IllegalArgumentException> { nextTarget(t, emptyList(), Rule(-1, 0)) }
    }
}
