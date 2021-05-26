package com.safidence.safidence

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        initViews()
    }

    private fun initViews() {
        loginBtn = findViewById(R.id.btnLogin)
        loginBtn.setOnClickListener {
            openMainActivity()
            finish()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}