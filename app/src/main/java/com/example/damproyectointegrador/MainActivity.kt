package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.damproyectointegrador.db.AppDBHelper
import com.example.damproyectointegrador.db.UserDAO

class MainActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Initialize views
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)

        // Initialize the database helper and DAO
        val dbHelper = AppDBHelper(this)
        val db = dbHelper.readableDatabase
        val userDAO = UserDAO(db)

        // Set click listener for the login button
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // If the username and password are correct, start the MenuActivity
            if (userDAO.login(username, password)) {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            else {
                val message = getString(R.string.incorrectLoginMessage)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}