package com.example.damproyectointegrador

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.MemberDAO
import com.example.damproyectointegrador.db.NoMemberDAO
import com.example.damproyectointegrador.entities.EClient
import com.example.damproyectointegrador.entities.EMember
import com.google.android.material.bottomnavigation.BottomNavigationView

class PayActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase
    private lateinit var memberDAO: MemberDAO
    private lateinit var noMemberDAO: NoMemberDAO

    private lateinit var etDNI: EditText
    private lateinit var rgMemberType: RadioGroup
    private lateinit var rbMember: RadioButton
    private lateinit var rbNoMember: RadioButton
    private lateinit var btnSearch: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Initialize views
        etDNI = findViewById(R.id.et_dni)
        rgMemberType = findViewById(R.id.rg_member_type)
        rbMember = findViewById(R.id.rb_member)
        rbNoMember = findViewById(R.id.rb_no_member)
        btnSearch = findViewById(R.id.btn_search)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Initialize database & DAOs
        db = AppDBHelper(this).writableDatabase
        memberDAO = MemberDAO(db)
        noMemberDAO = NoMemberDAO(db)

        // Set click listeners for radio buttons
        rbMember.setOnClickListener {
            updateRadioStyle()
        }
        rbNoMember.setOnClickListener {
            updateRadioStyle()
        }

        // Set click listener for the search button
        btnSearch.setOnClickListener {
            searchClient()
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

    private fun searchClient() {
        // Validate form
        if (!isValidForm()) return

        val dni = etDNI.text.toString().trim()

        if (rbMember.isChecked) {
            // Search for member
            val member = memberDAO.getMemberByDni(dni)
            if (member != null) {
                // Navigate to FeeActivity with member data
                val intent = Intent(this, FeeActivity::class.java)
                intent.putExtra("client_type", "member")
                intent.putExtra("firstname", member.firstname)
                intent.putExtra("lastname", member.lastname)
                intent.putExtra("dni", member.dni)
                intent.putExtra("due_fee_date", member.dueFeeDate)
                intent.putExtra("n_member", member.nMember)
                startActivity(intent)
            } else {
                showToast("No se encontró un socio con ese DNI")
            }
        } else {
            // Search for no member
            val noMember = noMemberDAO.getNoMemberByDni(dni)
            if (noMember != null) {
                // Navigate to FeeActivity with no member data
                val intent = Intent(this, FeeActivity::class.java)
                intent.putExtra("client_type", "nomember")
                intent.putExtra("firstname", noMember.firstname)
                intent.putExtra("lastname", noMember.lastname)
                intent.putExtra("dni", noMember.dni)
                intent.putExtra("due_fee_date", noMember.dueFeeDate)
                startActivity(intent)
            } else {
                showToast("No se encontró un no socio con ese DNI")
            }
        }
    }

    private fun isValidForm(): Boolean {
        if (!rbMember.isChecked && !rbNoMember.isChecked) {
            showToast("Es necesario seleccionar un tipo de cliente")
            return false
        }

        if (etDNI.text.isBlank()) {
            showToast("Es necesario ingresar el DNI")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}