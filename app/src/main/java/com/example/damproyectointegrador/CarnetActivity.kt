package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CarnetActivity : AppCompatActivity() {
    private lateinit var btnBuscar: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        btnBuscar = findViewById(R.id.btn_buscar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        btnBuscar.setOnClickListener {
            val intent = Intent(this, MembershipCardActivity::class.java)
            startActivity(intent)
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