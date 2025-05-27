package com.example.damproyectointegrador.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDBHelper(context: Context) : SQLiteOpenHelper(context, "clubDeportivo7", null, 10) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	username VARCHAR(20) NOT NULL,
            	password VARCHAR(15) NOT NULL,
            	status INTEGER CHECK (status in (0, 1)) DEFAULT 1
            );
        """.trimIndent())

        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'admin')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
}
