package com.safidence.safidence.ui.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseTenantData
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var etName: TextInputEditText
    private lateinit var etCnic: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etNationality: TextInputEditText
    private lateinit var etEmergencyName: TextInputEditText
    private lateinit var etEmergencyPhone: TextInputEditText
    private lateinit var btnRequest: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(root)
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

    private fun initViews(root:View){
        etName = root.findViewById(R.id.et_fullname)
        etCnic = root.findViewById(R.id.et_national_id)
        etPhone = root.findViewById(R.id.et_phone)
        etEmail = root.findViewById(R.id.et_email)
        etNationality = root.findViewById(R.id.et_nationality)
        etEmergencyName = root.findViewById(R.id.et_emergency_name)
        etEmergencyPhone = root.findViewById(R.id.et_emergency_phone)
        btnRequest = root.findViewById(R.id.btn_create_request)
    }

    private fun setupObserver() {
        profileViewModel.getResponse().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success")
                setData(it)
        })

        profileViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setData(data:ResponseTenantData){
        etName.setText(data.body[0].name)
        etCnic.setText(data.body[0].nic)
        etPhone.setText(data.body[0].phone)
        etEmail.setText(data.body[0].email)
        etNationality.setText(data.body[0].nationality.name)
        etEmergencyName.setText(data.body[0].profile.emergency_name)
        etEmergencyPhone.setText(data.body[0].profile.emergency_email)
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (progressDialog != null)
            progressDialog.dismiss()
    }
}