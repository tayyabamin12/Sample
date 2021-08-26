package com.safidence.safidence.ui.renew

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentRenewContractBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class RenewContractFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var renewContractViewModel: RenewContractViewModel
//    private lateinit var unitsSpinner: AutoCompleteTextView
//    private lateinit var etDate: TextInputEditText
//    private lateinit var etRenewDate: TextInputEditText
//    private lateinit var btnSubmit: Button
    private var _binding: FragmentRenewContractBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentRenewContractBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews(root)
        setupObserver()

        renewContractViewModel.getTenantUnits(SavePref(requireContext()).getAccessToken())

        return root
    }

    private fun setupViewModel() {
        renewContractViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(RenewContractViewModel::class.java)
    }

    private fun initViews(root: View) {
//        unitsSpinner = root.findViewById(R.id.ac_tv_units)
//        etDate = root.findViewById(R.id.et_date)
//        etRenewDate = root.findViewById(R.id.et_renew_date)
//        btnSubmit = root.findViewById(R.id.btn_create_request)

        binding.etRenewDate.setOnClickListener {
            updateErrorFields()
            selectTime()
        }

        binding.btnCreateRequest.setOnClickListener {
            updateErrorFields()
            if (!setErrorFields(binding.etDate.text.toString(), binding.etRenewDate.text.toString()))
                return@setOnClickListener
            renewContractViewModel.contractRequest(SavePref(requireContext()).getAccessToken(),
                binding.etDate.text.toString(), binding.etRenewDate.text.toString(),
                unitId, true)
            showProgressDialog()
        }
    }

    private fun setupObserver() {
        renewContractViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setUnitsSpinner(it)
            }
        })

        renewContractViewModel.getResponseTenantContractExpiry().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                binding.etDate.setText(it.body.expiry_date)
            }
        })

        renewContractViewModel.getResponseContractRequest().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

        renewContractViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
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
                renewContractViewModel.getTenantContractExpiry(token, unitId)
                showProgressDialog()
                updateErrorFields()
            }
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

    private fun selectTime() {
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this@RenewContractFragment, year, month,day)
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.etRenewDate.setText("$year-$month-$dayOfMonth")
    }


    private fun updateErrorFields() {
        binding.acTvUnits.error = null
        binding.etDate.error = null
        binding.etRenewDate.error = null
    }

    private fun setErrorFields(date: String, renewDate: String): Boolean {
        when {
            unitId == 0 -> {
                binding.acTvUnits.error = "* Required"
                return false
            }
            date == "" -> {
                binding.etDate.error = "* Required"
                return false
            }
            renewDate == "" -> {
                binding.etRenewDate.error = "* Required"
                return false
            }
            else -> return true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}