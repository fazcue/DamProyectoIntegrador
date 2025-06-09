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
import java.util.*

class FeeActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase
    private lateinit var paymentDAO: PaymentDAO

    private lateinit var tvNombre: TextView
    private lateinit var tvVencimiento: TextView
    private lateinit var tvAbono: TextView
    private lateinit var spinnerPaymentMethod: Spinner
    private lateinit var spinnerInstallments: Spinner
    private lateinit var btnPagar: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    private var clientType: String = ""
    private var clientDni: String = ""
    private var memberAmount: Double = 50000.0 // $50,000 para socios
    private var noMemberAmount: Double = 15000.0 // $15,000 para no socios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fee)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Initialize views
        tvNombre = findViewById(R.id.et_nombre)
        tvVencimiento = findViewById(R.id.et_vencimiento)
        tvAbono = findViewById(R.id.et_abono)
        spinnerPaymentMethod = findViewById(R.id.dropdown_pay)
        spinnerInstallments = findViewById(R.id.dropdown_fee)
        btnPagar = findViewById(R.id.btn_pagar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Initialize database & DAO
        db = AppDBHelper(this).writableDatabase
        paymentDAO = PaymentDAO(db)

        // Get client data from intent
        getClientDataFromIntent()

        // Set click listener for the pay button
        btnPagar.setOnClickListener {
            processPayment()
        }

        // Set click listener for the bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_volver -> {
                    val intent = Intent(this, PayActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_home -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_salir -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun getClientDataFromIntent() {
        clientType = intent.getStringExtra("client_type") ?: ""
        val firstname = intent.getStringExtra("firstname") ?: ""
        val lastname = intent.getStringExtra("lastname") ?: ""
        clientDni = intent.getStringExtra("dni") ?: ""
        val dueFeeDate = intent.getStringExtra("due_fee_date") ?: ""

        // Set client name
        tvNombre.text = "$firstname $lastname"

        // Set due date
        tvVencimiento.text = dueFeeDate

        // Set amount based on client type
        val amount = if (clientType == "member") memberAmount else noMemberAmount
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(amount)
        tvAbono.text = formattedAmount

        // Add member number if it's a member
        if (clientType == "member") {
            val nMember = intent.getIntExtra("n_member", 0)
            tvNombre.text = "$firstname $lastname (Socio Nº $nMember)"
        }
    }

    private fun processPayment() {
        val paymentMethod = spinnerPaymentMethod.selectedItem.toString()
        val installments = spinnerInstallments.selectedItem.toString().toInt()
        val amount = if (clientType == "member") memberAmount else noMemberAmount

        db.beginTransaction()
        var success = false

        try {
            // Record payment (you can implement this method in PaymentDAO if needed)
            paymentDAO.recordPayment(clientDni, amount, paymentMethod, installments)

            // Update due date based on client type
            success = if (clientType == "member") {
                paymentDAO.updateMemberDueDate(clientDni)
            } else {
                paymentDAO.updateNoMemberDueDate(clientDni)
            }

            if (success) {
                db.setTransactionSuccessful()
                showToast("Pago registrado correctamente")

                // Navigate back to menu
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
