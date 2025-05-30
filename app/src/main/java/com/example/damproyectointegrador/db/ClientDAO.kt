package com.example.damproyectointegrador.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.damproyectointegrador.entities.EClient

class ClientDAO(private val db: SQLiteDatabase) {
    fun isClient(dni: String): Boolean {
        val cursor = db.rawQuery(
            """
                SELECT * FROM clients
                WHERE dni = ?
            """.trimIndent(),
            arrayOf(dni)
        )
        val res = cursor.count > 0
        cursor.close()

        return res
    }

    fun registerClient(client: EClient): Long {
        val values = ContentValues().apply {
            put("firstname", client.firstname)
            put("lastname", client.lastname)
            put("dni", client.dni)
            put("due_fee_date", client.dueFeeDate)
        }

        return db.insert("clients", null, values)
    }

    fun setAsMember(clientId: Long): Long {
        val values = ContentValues().apply {
            put("n_licence", clientId * 100)
            put("id_client", clientId)
        }

        return db.insert("members", null, values)
    }

    fun setAsNoMember(clientId: Long): Long {
        val values = ContentValues().apply {
            put("id_client", clientId)
        }

        return db.insert("nomembers", null, values)
    }
}