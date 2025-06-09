package com.example.damproyectointegrador.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.text.SimpleDateFormat
import java.util.*

class PaymentDAO(private val db: SQLiteDatabase) {
    
    fun updateMemberDueDate(dni: String): Boolean {
        // Obtener la fecha de vencimiento actual
        val getCurrentDateQuery = "SELECT due_fee_date FROM clients WHERE dni = ?"
        val cursor = db.rawQuery(getCurrentDateQuery, arrayOf(dni))
        
        if (cursor.moveToFirst()) {
            val currentDueDateStr = cursor.getString(0)
            cursor.close()
            
            try {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val currentDueDate = dateFormat.parse(currentDueDateStr)
                val calendar = Calendar.getInstance()
                calendar.time = currentDueDate ?: Date()
                
                // Agregar 1 mes a la fecha de vencimiento
                calendar.add(Calendar.MONTH, 1)
                val newDueDateStr = dateFormat.format(calendar.time)
                
                // Actualizar la fecha en la base de datos
                val values = ContentValues().apply {
                    put("due_fee_date", newDueDateStr)
                }
                
                val rowsUpdated = db.update("clients", values, "dni = ?", arrayOf(dni))
                return rowsUpdated > 0
                
            } catch (e: Exception) {
                return false
            }
        } else {
            cursor.close()
            return false
        }
    }
    
    fun updateNoMemberDueDate(dni: String): Boolean {
        val calendar = Calendar.getInstance()
        // Agregar 1 día a la fecha actual
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val newDueDateStr = dateFormat.format(calendar.time)
        
        val values = ContentValues().apply {
            put("due_fee_date", newDueDateStr)
        }
        
        val rowsUpdated = db.update("clients", values, "dni = ?", arrayOf(dni))
        return rowsUpdated > 0
    }
    
    fun recordPayment(clientDni: String, amount: Double, paymentMethod: String, installments: Int): Boolean {
        // Esta función podría usarse para registrar el pago en una tabla de historial
        // Por ahora solo actualiza las fechas de vencimiento
        return true
    }
}