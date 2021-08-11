package com.safidence.safidence.ui.forgetpassword

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.ui.base.ViewModelFactory

class ForgetPassActivity : AppCompatActivity() {

    private lateinit var sendBtn: Button
    private lateinit var etCNIC: TextInputEditText
    private lateinit var forgetViewModel: ForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setContentView(R.layout.fragment_forget_password)
        initViews()
        setupLoginObserver()
    }

    private fun initViews() {
        sendBtn = findViewById(R.id.btn_send)
        etCNIC = findViewById(R.id.etNationalId)
        sendBtn.setOnClickListener {
            forgetViewModel.forgetPassword(etCNIC.text.toString())
            showProgressDialog()
        }
    }

    private fun setupViewModel() {
        forgetViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(ForgetViewModel::class.java)
    }

    private fun setupLoginObserver() {
        forgetViewModel.getLoginResponse().observe(this, Observer{
            dismissDialog()
            if (it.status.equals("success")) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                finish()
            }else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        })

        forgetViewModel.getExceptionResponse().observe(this, Observer {
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