package com.safidence.safidence.ui.changepassword

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentPasswordBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class PasswordFragment : Fragment() {

    private lateinit var passwordViewModel: PasswordViewModel
    private var _binding: FragmentPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
        setupObserver()

        return root
    }

    private fun setupViewModel() {
        passwordViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PasswordViewModel::class.java)
    }

    private fun initViews() {
        binding.btnUpdate.setOnClickListener {
            val token = SavePref(requireContext()).getAccessToken()
            val nic = SavePref(requireContext()).getUserCNIC()
            val oldPass = binding.tvCurrentPass.text.toString()
            val newPass = binding.tvNewPass.text.toString()
            val newConfirmPass = binding.tvConfirmPass.text.toString()

            if (oldPass == "") {
                Toast.makeText(requireContext(), "Current password should not be empty",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (newPass != "" && (newPass == newConfirmPass)) {
                showProgressDialog()
                passwordViewModel.updatePassword(token, nic, oldPass, newPass)
            }else {
                Toast.makeText(requireContext(), "New and confirm passwords must be matched",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupObserver() {
        passwordViewModel.getResponse().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        })

        passwordViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
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