package com.example.damproyectointegrador.db

import android.database.sqlite.SQLiteDatabase
import com.example.damproyectointegrador.entities.EClient

class NoMemberDAO(private val db: SQLiteDatabase) {
    fun getNoMemberByDni(dni: String): EClient? {
        val query = """
            SELECT c.firstname, c.lastname, c.dni, c.due_fee_date
            FROM clients c
            INNER JOIN nomembers nm ON c.id = nm.id_client
            WHERE c.dni = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(dni))
        var noMember: EClient? = null

        if (cursor.moveToFirst()) {
            noMember = EClient(
                firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname")),
                lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                dueFeeDate = cursor.getString(cursor.getColumnIndexOrThrow("due_fee_date"))
            )
        }
        cursor.close()
        return noMember
    }
}