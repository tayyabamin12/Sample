package com.safidence.safidence.ui.forgetpassword

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.databinding.FragmentForgetPasswordBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class ForgetPassActivity : AppCompatActivity() {

    private lateinit var forgetViewModel: ForgetViewModel
    private var _binding: FragmentForgetPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        _binding = FragmentForgetPasswordBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)
        initViews()
        setupObserver()
    }

    private fun initViews() {
        binding.btnSend.setOnClickListener {
            forgetViewModel.forgetPassword(binding.etNationalId.text.toString())
            showProgressDialog()
        }
    }

    private fun setupViewModel() {
        forgetViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(ForgetViewModel::class.java)
    }

    private fun setupObserver() {
        forgetViewModel.getLoginResponse().observe(this, Observer{
            dismissDialog()
            if (it.status == "success") {
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
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}