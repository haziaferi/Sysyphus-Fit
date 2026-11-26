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

// The migrations on the phone's own path: openSisyphusDatabase(context), through KeepFileOnCorruptionDriver and the
// framework SQLite, where the desktop tests use the bundled one.
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
    fun theAppMigratesAnOlderDatabaseKeepingEveryCell() {
        for (version in listOf(1, 2)) {
            val oldColumns = createSchema(version, file, driver)
            val before = raw(file, driver) { it.allCells(oldColumns) }

            val open = openSisyphusDatabase(context)
            open.database.close()
            assertFalse(open.recovered)
            assertEquals(emptyList(), setAside())

            raw(file, driver) { c ->
                assertEquals(before, c.allCells(oldColumns), "schema $version")
                assertMigrated(c, oldColumns, from = version)
            }
            file.parentFile!!.deleteRecursively()
        }
    }

    @Test
    fun aFailedMigrationOnThePhoneLeavesTheFileAsItWas() {
        for (version in listOf(1, 2)) {
            for ((how, damage) in migrationBreakers(version)) {
                val label = "schema $version, $how"
                val oldColumns = createSchema(version, file, driver, damage)
                val before = raw(file, driver) { it.untouchable(oldColumns) }

                assertFailedAsIntended(how, runCatching { openSisyphusDatabase(context) }.exceptionOrNull(), label)

                assertTrue(file.exists(), label)
                assertEquals(emptyList(), setAside(), label)
                assertEquals(before, raw(file, driver) { it.untouchable(oldColumns) }, label)
                file.parentFile!!.deleteRecursively()
            }
        }
    }
}
