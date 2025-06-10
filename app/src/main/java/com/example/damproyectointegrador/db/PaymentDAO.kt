package com.example.damproyectointegrador.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PaymentDAO(private val db: SQLiteDatabase) {

    // Actualiza la fecha de vencimiento de un SOCIO sumando 1 mes
    fun updateMemberDueDate(dni: String): Boolean {
        // Consulta la fecha de vencimiento actual del cliente
        val getCurrentDateQuery = "SELECT due_fee_date FROM clients WHERE dni = ?"
        val cursor = db.rawQuery(getCurrentDateQuery, arrayOf(dni))

        if (cursor.moveToFirst()) {
            val currentDueDateStr = cursor.getString(0)
            cursor.close()

            return try {
                // Parsear la fecha actual en formato yyyy/MM/dd
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val currentDueDate = dateFormat.parse(currentDueDateStr)

                // Agregar 1 mes a la fecha actual
                val calendar = Calendar.getInstance()
                calendar.time = currentDueDate ?: Date()
                calendar.add(Calendar.MONTH, 1)

                // Convertir la nueva fecha a string
                val newDueDateStr = dateFormat.format(calendar.time)

                // Preparar los valores para actualizar la base
                val values = ContentValues().apply {
                    put("due_fee_date", newDueDateStr)
                }

                // Ejecutar la actualización
                val rowsUpdated = db.update("clients", values, "dni = ?", arrayOf(dni))
                rowsUpdated > 0

            } catch (e: Exception) {
                false
            }
        } else {
            cursor.close()
            return false
        }
    }

    // Actualiza la fecha de vencimiento de un NO SOCIO sumando 1 DÍA
    fun updateNoMemberDueDate(dni: String): Boolean {
        // Consulta la fecha de vencimiento actual del no socio
        val getCurrentDateQuery = "SELECT due_fee_date FROM clients WHERE dni = ?"
        val cursor = db.rawQuery(getCurrentDateQuery, arrayOf(dni))

        if (cursor.moveToFirst()) {
            val currentDueDateStr = cursor.getString(0)
            cursor.close()

            return try {
                // Parsear la fecha actual en formato yyyy/MM/dd
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val currentDueDate = dateFormat.parse(currentDueDateStr)

                // Agregar 1 día a la fecha actual
                val calendar = Calendar.getInstance()
                calendar.time = currentDueDate ?: Date()
                calendar.add(Calendar.DAY_OF_MONTH, 1)

                // Convertir la nueva fecha a string
                val newDueDateStr = dateFormat.format(calendar.time)

                // Preparar los valores para actualizar la base
                val values = ContentValues().apply {
                    put("due_fee_date", newDueDateStr)
                }

                // Ejecutar la actualización
                val rowsUpdated = db.update("clients", values, "dni = ?", arrayOf(dni))
                rowsUpdated > 0

            } catch (e: Exception) {
                false
            }
        } else {
            cursor.close()
            return false
        }
    }
}
