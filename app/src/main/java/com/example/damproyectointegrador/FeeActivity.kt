package com.example.damproyectointegrador

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.PaymentDAO
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FeeActivity : AppCompatActivity() {

    // Base de datos y DAO
    private lateinit var db: SQLiteDatabase
    private lateinit var paymentDAO: PaymentDAO

    // Componentes de la interfaz
    private lateinit var tvNombre: TextView
    private lateinit var tvVencimiento: TextView
    private lateinit var tvAbono: TextView
    private lateinit var spinnerPaymentMethod: Spinner
    private lateinit var spinnerInstallments: Spinner
    private lateinit var btnPagar: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    // Datos del cliente
    private var clientType: String = ""
    private var clientDni: String = ""

    // Montos de abono
    private val memberAmount: Double = 50000.0     // $50.000 para socios
    private val noMemberAmount: Double = 15000.0   // $15.000 para no socios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fee)

        // Asegura que el foco no quede en un campo de texto
        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Inicializar vistas
        tvNombre = findViewById(R.id.et_nombre)
        tvVencimiento = findViewById(R.id.et_vencimiento)
        tvAbono = findViewById(R.id.et_abono)
        spinnerPaymentMethod = findViewById(R.id.dropdown_pay)
        spinnerInstallments = findViewById(R.id.dropdown_fee)
        btnPagar = findViewById(R.id.btn_pagar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Inicializar base de datos y DAO
        db = AppDBHelper(this).writableDatabase
        paymentDAO = PaymentDAO(db)

        // Obtener datos del cliente desde el intent
        getClientDataFromIntent()

        // Acción del botón "Pagar"
        btnPagar.setOnClickListener {
            processPayment()
        }

        // Navegación inferior
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_volver -> {
                    startActivity(Intent(this, PayActivity::class.java))
                    true
                }
                R.id.item_home -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                R.id.item_salir -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // Extrae y muestra los datos del cliente
    private fun getClientDataFromIntent() {
        clientType = intent.getStringExtra("client_type") ?: ""
        val firstname = intent.getStringExtra("firstname") ?: ""
        val lastname = intent.getStringExtra("lastname") ?: ""
        clientDni = intent.getStringExtra("dni") ?: ""
        val dueFeeDate = intent.getStringExtra("due_fee_date") ?: ""

        // Mostrar nombre completo
        tvNombre.text = getString(R.string.nombre_completo_placeholder, firstname, lastname)

        // Calcular y mostrar la próxima fecha de vencimiento
        val nextDueDate = calculateNextDueDate(dueFeeDate)
        tvVencimiento.text = nextDueDate

        // Mostrar monto a abonar según tipo de cliente
        val amount = if (clientType == "member") memberAmount else noMemberAmount
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(amount)
        tvAbono.text = formattedAmount
    }

    // Calcula la próxima fecha de vencimiento (1 mes para socios, 1 día para no socios)
    private fun calculateNextDueDate(currentDueDateStr: String): String {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDueDate = dateFormat.parse(currentDueDateStr)
            val calendar = Calendar.getInstance()
            calendar.time = currentDueDate ?: Date()

            if (clientType == "member") {
                calendar.add(Calendar.MONTH, 1)
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            dateFormat.format(calendar.time)
        } catch (e: Exception) {
            currentDueDateStr // Si ocurre un error, se muestra la fecha original
        }
    }

    // Procesa el pago y actualiza la base de datos
    private fun processPayment() {
        db.beginTransaction()

        try {
            val success = if (clientType == "member") {
                paymentDAO.updateMemberDueDate(clientDni)
            } else {
                paymentDAO.updateNoMemberDueDate(clientDni)
            }

            if (success) {
                db.setTransactionSuccessful()
                showToast("Pago registrado correctamente")

                // Volver al menú principal
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showToast("Error al actualizar la fecha de vencimiento")
            }
        } catch (e: Exception) {
            showToast("Error al procesar el pago: ${e.message}")
        } finally {
            db.endTransaction()
        }
    }

    // Muestra un mensaje al usuario
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
