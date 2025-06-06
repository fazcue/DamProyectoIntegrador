package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.MemberDAO

class CarnetActivity : AppCompatActivity() {

    private lateinit var btnBuscar: Button
    private lateinit var etDni: EditText
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var memberDAO: MemberDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        etDni = findViewById(R.id.DNI)
        btnBuscar = findViewById(R.id.btn_buscar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val dbHelper = AppDBHelper(this)
        val db = dbHelper.readableDatabase
        memberDAO = MemberDAO(db)

        btnBuscar.setOnClickListener {
            val dni = etDni.text.toString().trim()
            if (dni.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un número de DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
        }

            val member = memberDAO.getMemberByDni(dni)
            if (member != null) {
                val intent = Intent(this, MembershipCardActivity::class.java).apply {
                    putExtra("nombre", member.firstname)
                    putExtra("apellido", member.lastname)
                    putExtra("dni", member.dni)
                    putExtra("nSocio", member.nMember)
                    putExtra("fechaVencimiento", member.dueFeeDate)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            }
            etDni.text.clear()
            etDni.clearFocus()
        }

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
}