package com.safidence.safidence.ui.contract

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentContractBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class ContractFragment : Fragment() {

    private lateinit var contractViewModel: ContractViewModel
    private var _binding: FragmentContractBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentContractBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupObserver()
        initViews()

        contractViewModel.getTenantUnits(SavePref(requireContext()).getAccessToken())
        return root
    }

    private fun initViews() {
        binding.btnViewPdf.setOnClickListener {
            if (pdfUrl == "") {
                Toast.makeText(requireContext(), "pdf file not exist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle()
            bundle.putString("url", pdfUrl)
            findNavController().navigate(R.id.action_nav_contract_to_nav_pdf, bundle)
        }
    }

    private fun setupViewModel() {
        contractViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(ContractViewModel::class.java)
    }

    private var pdfUrl = ""
    private fun setupObserver() {
        contractViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setUnitsSpinner(it)
                binding.loading.visibility = View.GONE
            }
        })

        contractViewModel.getResponsePolicies().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                binding.etDate.setText(it.body.expiry_date)
                pdfUrl = it.body.file
            }
        })

        contractViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private var unitId = 0
    private fun setUnitsSpinner(selections: ResponseTenantUnits) {
        val unitTitles = ArrayList<String>()
        for (item in selections.body) {
            unitTitles.add(item.name)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, unitTitles)
        binding.acTvUnits.setAdapter(adapter)
        binding.acTvUnits.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                val token = SavePref(requireContext()).getAccessToken()
                unitId = selections.body[position].id
                contractViewModel.getContractDetails(token, unitId)
                showProgressDialog()
            }
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized && progressDialog != null)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}