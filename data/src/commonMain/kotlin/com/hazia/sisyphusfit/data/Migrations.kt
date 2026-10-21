package com.hazia.sisyphusfit.data

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/*
 * Every migration the app knows, applied by the one open (openDatabase). There is deliberately no
 * destructive fallback: a missing or failing migration stops the open with the file untouched.
 * The SQL is Room's own, copied from the exported schema (schemas/.../2.json), so Room's check
 * after migrating finds exactly the tables it would have created.
 */

/**
 * Schema 1 -> 2 (SPEC §4, stage 1a): additive only. Four new tables (module tags, labels and their
 * session links, body measures) and three new columns with a default of 0 (an exercise's
 * partial-reps switch, a set's power flag and partial-reps count); no schema-1 row is rewritten.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        listOf(
            "ALTER TABLE `exercise` ADD COLUMN `show_partial_reps` INTEGER NOT NULL DEFAULT 0",
            "ALTER TABLE `logged_set` ADD COLUMN `power` INTEGER NOT NULL DEFAULT 0",
            "ALTER TABLE `logged_set` ADD COLUMN `partial_reps` INTEGER NOT NULL DEFAULT 0",
            "CREATE TABLE IF NOT EXISTS `exercise_module` (`id` TEXT NOT NULL, `exercise_id` TEXT NOT NULL, " +
                "`module` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`exercise_id`) REFERENCES `exercise`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE )",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_exercise_module_exercise_id_module` ON `exercise_module` (`exercise_id`, `module`)",
            "CREATE TABLE IF NOT EXISTS `label` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `position` INTEGER NOT NULL, " +
                "`archived` INTEGER NOT NULL, PRIMARY KEY(`id`))",
            "CREATE TABLE IF NOT EXISTS `session_label` (`id` TEXT NOT NULL, `session_id` TEXT NOT NULL, " +
                "`label_id` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`session_id`) REFERENCES `session`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`label_id`) REFERENCES `label`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE )",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_session_label_session_id_label_id` ON `session_label` (`session_id`, `label_id`)",
            "CREATE INDEX IF NOT EXISTS `index_session_label_label_id` ON `session_label` (`label_id`)",
            "CREATE TABLE IF NOT EXISTS `body_measure` (`id` TEXT NOT NULL, `kind` TEXT NOT NULL, `value` INTEGER NOT NULL, " +
                "`unit` TEXT NOT NULL, `taken_at` INTEGER NOT NULL, `zone` TEXT NOT NULL, `day` TEXT NOT NULL, " +
                "`note` TEXT, PRIMARY KEY(`id`))",
            "CREATE INDEX IF NOT EXISTS `index_body_measure_kind_taken_at` ON `body_measure` (`kind`, `taken_at`)",
            "CREATE INDEX IF NOT EXISTS `index_body_measure_day` ON `body_measure` (`day`)",
        ).forEach(connection::execSQL)
    }
}

val MIGRATIONS = arrayOf(MIGRATION_1_2)
