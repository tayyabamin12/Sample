package com.safidence.safidence

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.safidence.safidence.ui.login.LoginActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}