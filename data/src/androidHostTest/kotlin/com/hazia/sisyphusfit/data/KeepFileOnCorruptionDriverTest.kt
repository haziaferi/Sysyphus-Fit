package com.hazia.sisyphusfit.data

import android.database.sqlite.SQLiteDatabaseCorruptException
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

// From Factotum's test of the same driver. The third test is the control: it must show the stock
// driver destroying the evidence, or the first test proves nothing.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class KeepFileOnCorruptionDriverTest {

    @get:Rule val tmp = TemporaryFolder()

    private val garbage = "this is not a database\n".toByteArray()

    @Test
    fun theAppSetsACorruptDatabaseAsideWithItsBytesIntact() {
        val context = RuntimeEnvironment.getApplication()
        val file = context.getDatabasePath(DATABASE_NAME).apply { parentFile!!.mkdirs(); writeBytes(garbage) }

        val open = openSisyphusDatabase(context)
        open.database.close()

        assertTrue(open.recovered, "the app never saw the corrupt file")
        val setAside = file.parentFile!!.listFiles { f -> f.name.startsWith(file.name + UNOPENABLE_SUFFIX) }!!
        assertContentEquals(garbage, setAside.single().readBytes())
        assertTrue(file.exists())
    }

    @Test
    fun theFrameworkReportsCorruptionAsItsTypedException() {
        val file = tmp.newFile("typed.db").apply { writeBytes(garbage) }

        val failure = runCatching {
            KeepFileOnCorruptionDriver().open(file.path).execSQL("SELECT count(*) FROM sqlite_master")
        }.exceptionOrNull()

        assertIs<SQLiteDatabaseCorruptException>(failure)
    }

    @Test
    fun theStockDriverDeletesACorruptFile() {
        val file = tmp.newFile("stock.db").apply { writeBytes(garbage) }

        AndroidSQLiteDriver().open(file.path).use { it.execSQL("SELECT count(*) FROM sqlite_master") }

        assertEquals(0, tmp.root.listFiles()!!.count { it.readBytes().contentEquals(garbage) })
    }
}
