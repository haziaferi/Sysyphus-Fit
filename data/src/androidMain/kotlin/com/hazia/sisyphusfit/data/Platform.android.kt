package com.hazia.sisyphusfit.data

import android.content.Context
import android.database.sqlite.SQLiteDatabaseCorruptException
import androidx.room.Room

fun openSisyphusDatabase(context: Context): DatabaseOpen<SisyphusDatabase> {
    val app = context.applicationContext
    return openDatabase(
        file = app.getDatabasePath(DATABASE_NAME),
        builder = { Room.databaseBuilder<SisyphusDatabase>(app, DATABASE_NAME) },
        driver = KeepFileOnCorruptionDriver(),
    )
}

actual fun isCorruptDatabase(failure: Throwable): Boolean =
    generateSequence(failure) { it.cause }.any { it is SQLiteDatabaseCorruptException }
