package com.example.damproyectointegrador

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.ClientDAO
import com.example.damproyectointegrador.entities.EClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase
    private lateinit var clientDAO: ClientDAO

    private lateinit var etDNI: EditText
    private lateinit var rgMemberType: RadioGroup
    private lateinit var rbMember: RadioButton
    private lateinit var rbNoMember: RadioButton
    private lateinit var etFirstname: EditText
    private lateinit var etLastname: EditText
    private lateinit var switchPhysicalFitness: Switch
    private lateinit var btnRegister: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Initialize views
        etDNI = findViewById(R.id.et_dni)
        rgMemberType = findViewById(R.id.rg_member_type)
        rbMember = findViewById(R.id.rb_member)
        rbNoMember = findViewById(R.id.rb_no_member)
        btnRegister = findViewById(R.id.btn_register)
        switchPhysicalFitness = findViewById(R.id.switch_physical_fitness)
        etFirstname = findViewById(R.id.et_firstname)
        etLastname = findViewById(R.id.et_lastname)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Initialize database & clientDAO
        db = AppDBHelper(this).writableDatabase
        clientDAO = ClientDAO(db)

        // Set click listener for the radio buttons
        rbMember.setOnClickListener {
            updateRadioStyle()
        }
        rbNoMember.setOnClickListener {
            updateRadioStyle()
        }

        // Set click listener for the register button
        btnRegister.setOnClickListener {
            registerClient()
        }

        // Set click listener for the bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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

    private fun updateRadioStyle() {
        val selectedBgColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val unselectedBgColor = ContextCompat.getColor(this, R.color.colorSecondaryLight)

        val selectedTextColor = ContextCompat.getColor(this, R.color.colorButtonText)
        val unselectedTextColor = ContextCompat.getColor(this, R.color.colorFormText)

        rbMember.setBackgroundColor(if (rbMember.isChecked) selectedBgColor else unselectedBgColor)
        rbMember.setTextColor(if (rbMember.isChecked) selectedTextColor else unselectedTextColor)

        rbNoMember.setBackgroundColor(if (rbNoMember.isChecked) selectedBgColor else unselectedBgColor)
        rbNoMember.setTextColor(if (rbNoMember.isChecked) selectedTextColor else unselectedTextColor)
    }

    private fun registerClient() {
        // Data validations
        if (!isValidForm()) return

        // Due date calculation
        val dueDate = getDueDate()

        // Create new client
        val dni = etDNI.text.toString().trim()
        val firstname = etFirstname.text.toString().trim()
        val lastname = etLastname.text.toString().trim()
        val newClient = EClient(firstname, lastname, dni, dueDate)

        // Register the client
        db.beginTransaction()
        var success = false
        val clientId = clientDAO.registerClient(newClient)

        // Register the client type if client was registered successfully
        if (clientId != -1L) {
            val res = if (rbMember.isChecked) clientDAO.setAsMember(clientId) else clientDAO.setAsNoMember(clientId)
            success = res != -1L
        }

        // Commit the transaction
        if (success) {
            db.setTransactionSuccessful()
            showToast("Cliente registrado correctamente")
            clearForm()
        } else {
            showToast("Error al registrar el cliente o tipo de cliente")
        }
        db.endTransaction()
    }

    private fun isValidForm(): Boolean {
        if (!rbMember.isChecked && !rbNoMember.isChecked) {
            showToast("Es necesario seleccionar un tipo de cliente")
            return false
        }

        if (etDNI.text.isBlank() || etFirstname.text.isBlank() || etLastname.text.isBlank()) {
            showToast("Es necesario que completes todos los campos.")
            return false
        }

        if (!switchPhysicalFitness.isChecked) {
            showToast("Es necesario presentar un apto físico para inscribirse en el club")
            return false
        }

        if (clientDAO.isClient(etDNI.text.toString().trim())) {
            showToast("Ya existe un cliente con ese DNI")
            return false
        }

        return true
    }

    private fun getDueDate(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearForm() {
        etDNI.text.clear()
        etFirstname.text.clear()
        etLastname.text.clear()
        switchPhysicalFitness.isChecked = false
        rgMemberType.clearCheck()
        updateRadioStyle()
    }
}