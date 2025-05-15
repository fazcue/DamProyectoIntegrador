package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {
    private lateinit var btnAltaCliente: Button
    private lateinit var btnPagarCuota: Button
    private lateinit var btnListarMorosos: Button
    private lateinit var btnEmitirCarnet: Button

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnAltaCliente = findViewById(R.id.btn_altaCliente)
        btnPagarCuota = findViewById(R.id.btn_pagarCuota)
        btnListarMorosos = findViewById(R.id.btn_listarMorosos)
        btnEmitirCarnet = findViewById(R.id.btn_emitirCarnet)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        btnAltaCliente.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, PayActivity::class.java)
            startActivity(intent)
        }

        btnListarMorosos.setOnClickListener {
            val intent = Intent(this, Debtors::class.java)
            startActivity(intent)
        }

        btnEmitirCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)
        }

        // Bottom navigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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