package com.hazia.sisyphusfit.data

import androidx.room.Dao
import androidx.room.Query
import com.hazia.sisyphusfit.core.Ids
import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/** The `setting` table: one value per key. Typed accessors live in [Settings], added as screens need them. */
@Dao
interface SettingDao {
    @Query("SELECT value FROM setting WHERE `key` = :key")
    fun value(key: String): Flow<String?>

    /** Sets [key] to [value]; [id] is used only when the key is new. */
    @Query("INSERT INTO setting (id, `key`, value) VALUES (:id, :key, :value) ON CONFLICT(`key`) DO UPDATE SET value = excluded.value")
    suspend fun put(id: String, key: String, value: String)
}

class Settings(private val db: Db, private val clock: WallClock) {
    /**
     * The modules the person chose (0.18): null until they choose, so nothing is assumed on; an empty set is
     * a choice of none. A name this build does not know (a backup from a newer one) is skipped.
     */
    fun modules(): Flow<Set<Module>?> = flow { emitAll(db().settingDao().value(MODULES)) }.map { value ->
        value?.split(',')?.mapNotNull { name -> Module.entries.firstOrNull { it.name == name } }?.toSet()
    }

    suspend fun chooseModules(modules: Set<Module>) {
        val value = Module.entries.filter { it in modules }.joinToString(",") { it.name }
        db().settingDao().put(Ids.ulid(clock.now()), MODULES, value)
    }

    private companion object {
        const val MODULES = "modules"
    }
}
