package com.example.damproyectointegrador.db

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import com.example.damproyectointegrador.entities.EMember
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("Range")
    fun getDebtors(): ArrayList<EMember> {
        val deudores = ArrayList<EMember>()

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val selectQuery = """
            SELECT c.firstname, c.lastname, c.dni, m.n_licence AS nMember, c.due_fee_date 
            FROM clients c, members m 
            WHERE c.id = m.id_client AND c.due_fee_date <= '$currentDate'
            """.trimIndent()

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use{  //garantiza el cierre automático del cursor
            while (it.moveToNext()) {
                val nombre  = cursor.getString(cursor.getColumnIndex("firstname"))
                val apellidos  = cursor.getString(cursor.getColumnIndex("lastname"))
                val dni  = cursor.getString(cursor.getColumnIndex("dni"))
                val dueFeeDate  = cursor.getString(cursor.getColumnIndex("due_fee_date"))
                val nMember  = cursor.getString(cursor.getColumnIndex("nMember"))
                val miembro = EMember(nombre, apellidos, dni, dueFeeDate, nMember.toInt())
                deudores.add(miembro)
            }
        }

        return deudores
    }
}

