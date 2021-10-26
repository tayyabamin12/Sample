package com.safidence.safidence.ui.profile

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
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseTenantData
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentProfileBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
        setupObserver()

        profileViewModel.getTenantData(SavePref(requireContext()).getAccessToken())
        showProgressDialog()

        return root
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(ProfileViewModel::class.java)
    }

    private fun initViews(){
        binding.btnCreateRequest.setOnClickListener {
            updateErrorFields()
            val token = SavePref(requireContext()).getAccessToken()
            val phone = binding.etPhone.text.toString()
            val email = binding.etEmail.text.toString()
            val emergencyName = binding.etEmergencyName.text.toString()
            val emergencyPhone = binding.etEmergencyPhone.text.toString()

            if (!setErrorFields(phone, email, emergencyName, emergencyPhone))
                return@setOnClickListener
            profileViewModel.saveTenantData(token, phone, email, emergencyName, emergencyPhone)
            showProgressDialog()
        }
    }

    private fun setupObserver() {
        profileViewModel.getResponse().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success")
                setData(it)
        })

        profileViewModel.getResponseSaveProfile().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            if (it.status == "success")
                findNavController().navigateUp()
        })

        profileViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setData(data:ResponseTenantData){
        try {
            binding.etFullname.setText(data.body[0].name)
            binding.etNationalId.setText(data.body[0].nic)
            binding.etPhone.setText(data.body[0].phone)
            binding.etEmail.setText(data.body[0].email)
            binding.etNationality.setText(data.body[0].nationality.name)
            binding.etEmergencyName.setText(data.body[0].profile.emergency_name)
            binding.etEmergencyPhone.setText(data.body[0].profile.emergency_phone)
        } catch (e: Exception) {
        }
    }

    private fun updateErrorFields() {
        binding.etPhone.error = null
        binding.etEmail.error = null
        binding.etEmergencyName.error = null
        binding.etEmergencyPhone.error = null
    }

    private fun setErrorFields(phone: String, email: String, emer_name: String,
                               emer_phone: String): Boolean {
        when {
            phone == "" -> {
                binding.etPhone.error = "* Required"
                return false
            }
            email == "" -> {
                binding.etEmail.error = "* Required"
                return false
            }
            emer_name == "" -> {
                binding.etEmergencyName.error = "* Required"
                return false
            }
            emer_phone == "" -> {
                binding.etEmergencyPhone.error = "* Required"
                return false
            }
            else -> return true
        }
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