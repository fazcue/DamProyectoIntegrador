package com.example.damproyectointegrador

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.MemberDAO
import com.example.damproyectointegrador.db.NoMemberDAO
import com.google.android.material.bottomnavigation.BottomNavigationView

class PayActivity : AppCompatActivity() {

    // Declaración de variables para la base de datos y DAOs
    private lateinit var db: SQLiteDatabase
    private lateinit var memberDAO: MemberDAO
    private lateinit var noMemberDAO: NoMemberDAO

    // Declaración de vistas
    private lateinit var etDNI: EditText
    private lateinit var rgMemberType: RadioGroup
    private lateinit var rbMember: RadioButton
    private lateinit var rbNoMember: RadioButton
    private lateinit var btnSearch: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        // Para evitar que el foco quede en el campo de texto al iniciar y se abra el teclado automáticamente
        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Inicialización de vistas
        etDNI = findViewById(R.id.et_dni)
        rgMemberType = findViewById(R.id.rg_member_type)
        rbMember = findViewById(R.id.rb_member)
        rbNoMember = findViewById(R.id.rb_no_member)
        btnSearch = findViewById(R.id.btn_search)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Inicialización de base de datos y DAOs
        db = AppDBHelper(this).writableDatabase
        memberDAO = MemberDAO(db)
        noMemberDAO = NoMemberDAO(db)

        // Listeners para cambiar el estilo visual de los RadioButton cuando se seleccionan
        rbMember.setOnClickListener { updateRadioStyle() }
        rbNoMember.setOnClickListener { updateRadioStyle() }

        // Listener del botón de búsqueda
        btnSearch.setOnClickListener { searchClient() }

        // Navegación inferior
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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

    // Cambia los colores de los radio buttons según cuál esté seleccionado
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

    // Realiza la búsqueda del cliente y lo envía a FeeActivity con los datos correspondientes
    private fun searchClient() {
        if (!isValidForm()) return

        val dni = etDNI.text.toString().trim()

        if (rbMember.isChecked) {
            // Buscar socio
            val member = memberDAO.getMemberByDni(dni)
            if (member != null) {
                val intent = Intent(this, FeeActivity::class.java).apply {
                    putExtra("client_type", "member")
                    putExtra("firstname", member.firstname)
                    putExtra("lastname", member.lastname)
                    putExtra("dni", member.dni)
                    putExtra("due_fee_date", member.dueFeeDate)
                    putExtra("n_member", member.nMember)
                }
                startActivity(intent)
            } else {
                showToast("No se encontró un ¨socio¨ con ese DNI")
            }
        } else {
            // Buscar no socio
            val noMember = noMemberDAO.getNoMemberByDni(dni)
            if (noMember != null) {
                val intent = Intent(this, FeeActivity::class.java).apply {
                    putExtra("client_type", "nomember")
                    putExtra("firstname", noMember.firstname)
                    putExtra("lastname", noMember.lastname)
                    putExtra("dni", noMember.dni)
                    putExtra("due_fee_date", noMember.dueFeeDate)
                }
                startActivity(intent)
            } else {
                showToast("No se encontró un ¨no socio¨ con ese DNI")
            }
        }
    }

    // Valida si el formulario está completo
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

    // Muestra un mensaje al usuario
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
