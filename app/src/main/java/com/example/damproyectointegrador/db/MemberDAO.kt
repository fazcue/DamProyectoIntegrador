package com.example.damproyectointegrador.db

import android.database.sqlite.SQLiteDatabase
import com.example.damproyectointegrador.entities.EMember

class MemberDAO(private val db: SQLiteDatabase) {
    fun getMemberByDni(dni: String): EMember? {
        val query = """
            SELECT c.firstname, c.lastname, c.dni, m.n_licence AS nMember, c.due_fee_date
            FROM clients c
            INNER JOIN members m ON c.id = m.id_client
            WHERE c.dni = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(dni))
        var member: EMember? = null

        if (cursor.moveToFirst()) {
            member = EMember(
                firstname = cursor.getString(cursor.getColumnIndexOrThrow("firstname")),
                lastname = cursor.getString(cursor.getColumnIndexOrThrow("lastname")),
                dni = cursor.getString(cursor.getColumnIndexOrThrow("dni")),
                dueFeeDate = cursor.getString(cursor.getColumnIndexOrThrow("due_fee_date")),
                nMember = cursor.getInt(cursor.getColumnIndexOrThrow("nMember"))
            )
        }
        cursor.close()
        return member
    }
}