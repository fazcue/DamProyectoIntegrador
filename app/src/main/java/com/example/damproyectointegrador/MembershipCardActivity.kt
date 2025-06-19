package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MembershipCardActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnImprimir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership_card)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        val tvNombre = findViewById<TextView>(R.id.nombre)
        val tvDni = findViewById<TextView>(R.id.dni)
        val tvNroSocio = findViewById<TextView>(R.id.nroSocio)
        val tvVencimiento = findViewById<TextView>(R.id.vencimiento)
        btnImprimir = findViewById(R.id.btn_imprimir)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val dni = intent.getStringExtra("dni")
        val nSocio = intent.getIntExtra("nSocio", 0)
        val fechaVencimiento = intent.getStringExtra("fechaVencimiento")

        tvNombre.text = getString(R.string.nombre_completo_placeholder, nombre, apellido)
        tvDni.text = dni
        tvNroSocio.text = nSocio.toString()
        tvVencimiento.text = fechaVencimiento

        btnImprimir.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("message", "Carnet impreso correctamente!")
            }
            startActivity(intent)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_volver -> {
                    val intent = Intent(this, CarnetActivity::class.java)
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
}