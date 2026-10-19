package com.hazia.sisyphusfit.core

/** What a routine item asks for. Null = not part of this exercise's target. */
data class Target(val sets: Int, val reps: Int?, val weightGrams: Long?)

/** One done set of the session being judged. */
data class DoneSet(val reps: Int?, val weightGrams: Long?)

/** A progression rule (harvest row LL5): after a session where every set hit its target, add this. */
data class Rule(val addWeightGrams: Long, val addReps: Int)

/**
 * Next session's target (harvest rows LL5 and GM6). It advances only when the session holds at
 * least [Target.sets] done sets and every one of the first [Target.sets] met the reps and weight
 * asked for; otherwise the target stays. Never lowers a target: a deload is a separate row (IV1),
 * out of the current scope.
 */
fun nextTarget(target: Target, done: List<DoneSet>, rule: Rule): Target {
    require(target.sets > 0) { "a target with no sets" }
    require(rule.addWeightGrams >= 0 && rule.addReps >= 0) { "a rule may not lower a target" }
    if (done.size < target.sets) return target
    val hit = done.take(target.sets).all { s ->
        (target.reps == null || (s.reps ?: 0) >= target.reps) &&
            (target.weightGrams == null || (s.weightGrams ?: 0) >= target.weightGrams)
    }
    if (!hit) return target
    return target.copy(
        reps = target.reps?.plus(rule.addReps),
        weightGrams = target.weightGrams?.plus(rule.addWeightGrams),
    )
}
