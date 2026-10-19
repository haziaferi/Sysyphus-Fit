package com.hazia.sisyphusfit.core

import kotlin.random.Random

/**
 * Row ids are ULIDs (SPEC §4): 48 bits of milliseconds then 80 random bits, written as 26
 * Crockford base-32 characters. They sort by creation time, and a restored or imported row never
 * collides with a local one.
 */
object Ids {
    private const val ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"
    private const val MAX_TIME = (1L shl 48) - 1

    fun ulid(nowMillis: Long, random: Random = Random.Default): String {
        require(nowMillis in 0..MAX_TIME) { "time out of ULID range: $nowMillis" }
        val out = CharArray(26)
        var t = nowMillis
        for (i in 9 downTo 0) {
            out[i] = ALPHABET[(t and 31).toInt()]
            t = t ushr 5
        }
        val bytes = random.nextBytes(10)
        // 80 random bits as 16 five-bit groups.
        var acc = 0L
        var bits = 0
        var pos = 10
        for (b in bytes) {
            acc = (acc shl 8) or (b.toLong() and 0xFF)
            bits += 8
            while (bits >= 5) {
                bits -= 5
                out[pos++] = ALPHABET[((acc ushr bits) and 31).toInt()]
            }
        }
        return out.concatToString()
    }

    /** The creation time a ULID carries. */
    fun timeOf(ulid: String): Long {
        require(ulid.length == 26) { "not a ULID: $ulid" }
        var t = 0L
        for (i in 0 until 10) {
            val v = ALPHABET.indexOf(ulid[i])
            require(v >= 0) { "not a ULID: $ulid" }
            t = (t shl 5) or v.toLong()
        }
        return t
    }
}
