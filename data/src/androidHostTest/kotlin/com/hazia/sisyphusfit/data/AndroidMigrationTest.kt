package com.hazia.sisyphusfit.data

import androidx.sqlite.driver.AndroidSQLiteDriver
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// The 1 -> 2 migration on the phone's own path: openSisyphusDatabase(context), through
// KeepFileOnCorruptionDriver and the framework SQLite, where the desktop tests use the bundled one.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AndroidMigrationTest {

    private val context = RuntimeEnvironment.getApplication()
    private val file = context.getDatabasePath(DATABASE_NAME)
    private val driver = AndroidSQLiteDriver()

    private fun setAside() = file.parentFile!!.list()!!.filter { UNOPENABLE_SUFFIX in it }

    @AfterTest fun clean() {
        file.parentFile!!.deleteRecursively()
    }

    @Test
    fun theAppMigratesASchema1DatabaseKeepingEveryCell() {
        val v1Columns = createSchema1(file, driver)
        val before = raw(file, driver) { it.allCells(v1Columns) }

        val open = openSisyphusDatabase(context)
        open.database.close()
        assertFalse(open.recovered)
        assertEquals(emptyList(), setAside())

        raw(file, driver) { c ->
            assertEquals(before, c.allCells(v1Columns))
            assertMigrated(c, v1Columns)
        }
    }

    @Test
    fun aFailedMigrationOnThePhoneLeavesTheFileAsItWas() {
        for ((how, damage) in migrationBreakers) {
            val v1Columns = createSchema1(file, driver, damage)
            val before = raw(file, driver) { it.untouchable(v1Columns) }

            assertTrue(runCatching { openSisyphusDatabase(context) }.isFailure, "$how: the open should fail")

            assertTrue(file.exists(), how)
            assertEquals(emptyList(), setAside(), how)
            assertEquals(before, raw(file, driver) { it.untouchable(v1Columns) }, how)
            file.parentFile!!.deleteRecursively()
        }
    }
}
