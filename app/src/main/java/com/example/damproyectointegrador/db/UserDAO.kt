package com.example.damproyectointegrador.db

import android.database.sqlite.SQLiteDatabase

class UserDAO(private val db: SQLiteDatabase) {
    fun login(username: String, password: String): Boolean {
        val cursor = db.rawQuery(
            """
                SELECT * FROM users
                WHERE username = ?
                AND password = ?
                AND status = 1
            """.trimIndent(),
            arrayOf(username, password)
        )
        val res = cursor.count > 0
        cursor.close()
        return res
    }
}