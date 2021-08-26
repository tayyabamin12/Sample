package com.safidence.safidence.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.MainActivity
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentLoginBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import com.safidence.safidence.ui.forgetpassword.ForgetPassActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()

        _binding = FragmentLoginBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)

        if (SavePref(this).getAccessToken() != "")
            openMainActivity()

        initViews()
        setupLoginObserver()
    }

    private fun initViews() {
        binding.btnLogin.setOnClickListener {
            if (binding.etNationalId.text.toString() == "" || binding.etPassword.text.toString() == "") {
                Toast.makeText(this, "Please provide valid national id or password",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            loginViewModel.login(binding.etNationalId.text.toString(), binding.etPassword.text.toString())
            showProgressDialog()
        }

        binding.tvForgotPwd.setOnClickListener {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}