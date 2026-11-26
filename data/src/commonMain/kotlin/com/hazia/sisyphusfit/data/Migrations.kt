package com.hazia.sisyphusfit.data

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/*
 * Every migration the app knows, applied by the one open (openDatabase). There is deliberately no
 * destructive fallback: a missing or failing migration stops the open with the file untouched.
 * The SQL is Room's own, copied from the exported schemas (schemas/.../2.json, 3.json), so Room's check
 * after migrating finds exactly the tables it would have created.
 */

/**
 * Schema 1 -> 2 (SPEC:R28, SPEC:R33, stage 1a): additive only. Four new tables (module tags, labels and their
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

/**
 * Gives every set whose kind is still empty its exercise's type (SPEC:R121): the sets logged before schema 3, after
 * the migration adds the column and after any restore. An exercise always exists: foreign keys are on for every
 * connection since schema 1 and a set's exercise is RESTRICT. Were one missing, the NOT NULL column refuses the fill
 * and the open stops with the file kept (SPEC:R33), rather than storing a set under no type.
 */
const val FILL_SET_KINDS =
    "UPDATE `logged_set` SET `kind` = (SELECT `kind` FROM `exercise` WHERE `exercise`.`id` = `logged_set`.`exercise_id`) WHERE `kind` = ''"

/**
 * Schema 2 -> 3 (SPEC:R121, SPEC:R33): additive. An amount's time and module on a session, a set's type, an exercise's
 * rest and note (SPEC:R103), and three new tables: steps, categories and their exercise links. No schema-2 cell changes;
 * the new `kind` column is filled from each set's exercise.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        listOf(
            "ALTER TABLE `exercise` ADD COLUMN `rest_seconds` INTEGER",
            "ALTER TABLE `exercise` ADD COLUMN `note` TEXT",
            "ALTER TABLE `session` ADD COLUMN `seconds` INTEGER",
            "ALTER TABLE `session` ADD COLUMN `module` TEXT",
            "ALTER TABLE `logged_set` ADD COLUMN `kind` TEXT NOT NULL DEFAULT ''",
            FILL_SET_KINDS,
            "CREATE TABLE IF NOT EXISTS `steps` (`id` TEXT NOT NULL, `day` TEXT NOT NULL, `steps` INTEGER NOT NULL, " +
                "`source` TEXT NOT NULL, PRIMARY KEY(`id`))",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_steps_day_source` ON `steps` (`day`, `source`)",
            "CREATE TABLE IF NOT EXISTS `category` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))",
            "CREATE TABLE IF NOT EXISTS `exercise_category` (`id` TEXT NOT NULL, `exercise_id` TEXT NOT NULL, " +
                "`category_id` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`exercise_id`) REFERENCES `exercise`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE )",
            "CREATE UNIQUE INDEX IF NOT EXISTS `index_exercise_category_exercise_id_category_id` ON `exercise_category` " +
                "(`exercise_id`, `category_id`)",
            "CREATE INDEX IF NOT EXISTS `index_exercise_category_category_id` ON `exercise_category` (`category_id`)",
        ).forEach(connection::execSQL)
    }
}

val MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
