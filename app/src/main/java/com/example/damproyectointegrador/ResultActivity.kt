package com.example.damproyectointegrador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var tvMessage: TextView
    private lateinit var btnGoBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val baseLayout = findViewById<View>(R.id.base_layout)
        baseLayout.requestFocus()

        // Initialize views
        tvMessage = findViewById(R.id.tv_message)
        btnGoBack = findViewById(R.id.btn_goBack)

        // Get the message from the intent
        val message = intent.getStringExtra("message")
        tvMessage.text = message

        // Set click listener for the login button
        btnGoBack.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}