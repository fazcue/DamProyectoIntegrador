package com.example.damproyectointegrador.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDBHelper(context: Context) : SQLiteOpenHelper(context, "clubDeportivo8", null, 23) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	username TEXT NOT NULL,
            	password TEXT NOT NULL,
            	status INTEGER CHECK (status in (0, 1)) DEFAULT 1
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS clients (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	firstname TEXT NOT NULL,
            	lastname TEXT NOT NULL,
            	dni TEXT UNIQUE NOT NULL,
            	due_fee_date TEXT NOT NULL
            );
            """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS members (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	n_licence INTEGER UNIQUE NOT NULL,
            	id_client INTEGER NOT NULL,
            	CONSTRAINT fk_member_client_id FOREIGN KEY(id_client) REFERENCES clients(id)
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS nomembers (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
            	id_client INTEGER NOT NULL,
            	CONSTRAINT fk_noMember_client_id FOREIGN KEY(id_client) REFERENCES clients(id)
            );
        """.trimIndent())

        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'admin')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS clients")
        db.execSQL("DROP TABLE IF EXISTS members")
        db.execSQL("DROP TABLE IF EXISTS nomembers")
        onCreate(db)
    }
}