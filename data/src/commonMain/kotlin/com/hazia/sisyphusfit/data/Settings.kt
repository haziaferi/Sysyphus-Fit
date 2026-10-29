package com.hazia.sisyphusfit.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hazia.sisyphusfit.core.Ids
import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/** The `setting` table: one value per key. Typed accessors live in [Settings], added as screens need them. */
@Dao
abstract class SettingDao {
    @Query("SELECT value FROM setting WHERE `key` = :key")
    abstract fun value(key: String): Flow<String?>

    @Query("SELECT value FROM setting WHERE `key` = :key")
    protected abstract suspend fun current(key: String): String?

    /** Sets [key] to [value]; [id] is used only when the key is new. */
    @Query("INSERT INTO setting (id, `key`, value) VALUES (:id, :key, :value) ON CONFLICT(`key`) DO UPDATE SET value = excluded.value")
    abstract suspend fun put(id: String, key: String, value: String)

    /** Sets [key] to what [change] makes of its current value, read and written in one transaction. */
    @Transaction
    open suspend fun change(id: String, key: String, change: (String?) -> String) = put(id, key, change(current(key)))
}

class Settings(private val db: Db, private val clock: WallClock) {
    /**
     * The modules the person chose (0.18): null until they choose, so nothing is assumed on; an empty set is
     * a choice of none. A name this build does not know (a backup from a newer one) is skipped.
     */
    fun modules(): Flow<Set<Module>?> = flow { emitAll(db().settingDao().value(MODULES)) }.map { value ->
        value?.split(',')?.mapNotNull { name -> Module.entries.firstOrNull { it.name == name } }?.toSet()
    }

    /** Records the choice; names a newer build wrote are kept after it, untouched (M4). */
    suspend fun chooseModules(modules: Set<Module>) {
        val chosen = Module.entries.filter { it in modules }.map { it.name }
        db().settingDao().change(Ids.ulid(clock.now()), MODULES) { stored ->
            val unknown = stored?.split(',')?.filter { name -> name.isNotEmpty() && Module.entries.none { it.name == name } }.orEmpty()
            (chosen + unknown).joinToString(",")
        }
    }

    private companion object {
        const val MODULES = "modules"
    }
}
