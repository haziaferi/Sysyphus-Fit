package com.hazia.sisyphusfit.ui

import java.io.File
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** SPEC 0.40: the app bundles Sisifo's released files, v1.0 of github.com/haziaferi/sisifo, unchanged. */
class FontFilesTest {
    private val res = File("src/main/res")

    private fun sha256(f: File) =
        MessageDigest.getInstance("SHA-256").digest(f.readBytes()).joinToString("") { "%02x".format(it) }

    @Test
    fun fontFilesAreTheRelease() {
        assertEquals("6abb50cdd398d256a6d34eef6f3e697411c7212e51b9f8f51a523d7be3b53ad2", sha256(File(res, "font/sisifo.ttf")))
        assertEquals("89f60b26ea58847d4a67846405974c0b8be3277580b79b934132cf91a694882d", sha256(File(res, "font/sisifo_italic.ttf")))
        assertTrue(File("src/main/assets/licences/OFL-Sisifo.txt").readText().contains("SIL OPEN FONT LICENSE Version 1.1"))
    }
}
