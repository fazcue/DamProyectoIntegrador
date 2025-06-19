package com.example.damproyectointegrador

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.MemberDAO
import com.google.android.material.bottomnavigation.BottomNavigationView

class DebtorsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var memberDAO: MemberDAO
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DebtorsAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debtors)

        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        tvMensaje.text = ""

        // Configuración inicial
        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Inicialización de la base de datos
        val dbHelper = AppDBHelper(this)
        val db = dbHelper.readableDatabase
        memberDAO = MemberDAO(db)

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.debtorsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener y mostrar deudores
        val debtors = memberDAO.getDebtors()
        adapter = DebtorsAdapter(debtors)

        if (adapter.itemCount == 0){
            tvMensaje.text = "No se registran deudores a la fecha"
        }
        else {
            recyclerView.adapter = adapter
        }

        // Configuración del menú inferior
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
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
}

