package com.hazia.sisyphusfit.core

/**
 * The training modules (SPEC §8, decisions 0.9, 0.10). A module is a tag on an exercise, and an
 * exercise may carry several (0.23); the database stores the [name].
 */
enum class Module { DAILY_MOVEMENT, STRENGTH, STAMINA, MOBILITY_BALANCE, BODY_MEASURES }

/** One done set in the week: its session, its exercise and its time in seconds (0 when untimed). */
data class WeekSet(val sessionId: String, val exerciseId: String, val seconds: Long)

/** One module's week: the sessions that touched it, its sets, its time in seconds. */
data class ModuleWeek(val sessions: Int, val sets: Int, val seconds: Long)

/**
 * "This week" per module, a mixed session counted by its parts (decision 0.24): each set counts,
 * whole, under every module tag its exercise carries; a session counts once under each module it
 * touched. There is deliberately no combined total (SPEC §7): the result is one entry per module,
 * never summed. An exercise with no tag counts nowhere; a module with nothing done is absent.
 */
fun weekByModule(sets: List<WeekSet>, tags: Map<String, Set<Module>>): Map<Module, ModuleWeek> {
    require(sets.all { it.seconds >= 0 }) { "a negative time" }
    val sessions = mutableMapOf<Module, MutableSet<String>>()
    val counts = mutableMapOf<Module, Int>()
    val seconds = mutableMapOf<Module, Long>()
    for (s in sets) {
        for (m in tags[s.exerciseId].orEmpty()) {
            sessions.getOrPut(m) { mutableSetOf() } += s.sessionId
            counts[m] = (counts[m] ?: 0) + 1
            seconds[m] = (seconds[m] ?: 0) + s.seconds
        }
    }
    return Module.entries.filter { it in counts }
        .associateWith { ModuleWeek(sessions.getValue(it).size, counts.getValue(it), seconds.getValue(it)) }
}
