package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debtors)

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
        recyclerView.adapter = adapter

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













/*


package com.example.damproyectointegrador

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.CursorAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.MemberDAO
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import com.example.damproyectointegrador.DebtorAdapter

class DebtorsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var memberDAO: MemberDAO

    private lateinit var listaDeudores: RecyclerView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DebtorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debtors)
        listaDeudores = findViewById(R.id.listaDeudores)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        val dbHelper = AppDBHelper(this)
        val db = dbHelper.readableDatabase
        memberDAO = MemberDAO(db)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

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

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val debtors = memberDAO.getDebtors()

//        recyclerView = findViewById(R.id.listaDeudores)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DebtorAdapter(debtors)
        recyclerView.adapter = adapter

//        debtors.forEach { member ->
//            Log.d("ROOM_EXPIRED", "${member.firstname} ${member.lastname} - ${member.dueFeeDate}")
//        }

    }

//    inner class CursorAdapterListView(context: Context, cursor: Cursor) :
//        CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER) {
//
//    }
}

*/