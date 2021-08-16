package com.safidence.safidence.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.safidence.safidence.MainActivity
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory
import com.safidence.safidence.ui.forgetpassword.ForgetPassActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var etCNIC: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tvForgetPass: TextView
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setContentView(R.layout.fragment_login)

        if (SavePref(this).getAccessToken() != "")
            openMainActivity()

        initViews()
        setupLoginObserver()
    }

    private fun initViews() {
        loginBtn = findViewById(R.id.btnLogin)
        etCNIC = findViewById(R.id.etNationalId)
        etPassword = findViewById(R.id.etPassword)
        tvForgetPass = findViewById(R.id.tvForgotPwd)
        loginBtn.setOnClickListener {
            if (etCNIC.text.toString() == "" || etPassword.text.toString() == "") {
                Toast.makeText(this, "Please provide valid national id or password",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            loginViewModel.login(etCNIC.text.toString(), etPassword.text.toString())
            showProgressDialog()
        }

        tvForgetPass.setOnClickListener {
            startActivity(Intent(this, ForgetPassActivity::class.java))
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(LoginViewModel::class.java)
    }

    private fun setupLoginObserver() {
        loginViewModel.getLoginResponse().observe(this, Observer{
            dismissDialog()
            if (it.success) {
                SavePref(this).saveLogin(it)
                openMainActivity()
            }else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show()
            }
        })

        loginViewModel.getExceptionResponse().observe(this, Observer {
            dismissDialog()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (progressDialog != null)
            progressDialog.dismiss()
    }
}