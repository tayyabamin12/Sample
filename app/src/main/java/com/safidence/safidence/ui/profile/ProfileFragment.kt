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
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
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
        binding.etFullname.setText(data.body[0].name)
        binding.etNationalId.setText(data.body[0].nic)
        binding.etPhone.setText(data.body[0].phone)
        binding.etEmail.setText(data.body[0].email)
        binding.etNationality.setText(data.body[0].nationality.name)
        binding.etEmergencyName.setText(data.body[0].profile.emergency_name)
        binding.etEmergencyPhone.setText(data.body[0].profile.emergency_email)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}