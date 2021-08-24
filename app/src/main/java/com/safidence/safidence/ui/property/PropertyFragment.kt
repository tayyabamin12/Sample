package com.safidence.safidence.ui.property

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.model.ResponseUnitDetails
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory

class PropertyFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var unitsSpinner: AutoCompleteTextView
    private lateinit var etBuildingName: TextInputEditText
    private lateinit var etAccommodation: TextInputEditText
    private lateinit var etSpace: TextInputEditText
    private lateinit var etParking: TextInputEditText
    private lateinit var etAddress: TextInputEditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        propertyViewModel =
                ViewModelProvider(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_property, container, false)
        initViews(root)
        setupObserver()

        propertyViewModel.getTenantUnits(SavePref(requireContext()).getAccessToken())

        return root
    }

    private fun setupViewModel() {
        propertyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PropertyViewModel::class.java)
    }

    private fun initViews(root:View) {
        unitsSpinner = root.findViewById(R.id.ac_tv_units)
        etBuildingName = root.findViewById(R.id.et_bname)
        etAccommodation = root.findViewById(R.id.et_accommodation)
        etSpace = root.findViewById(R.id.et_space)
        etParking = root.findViewById(R.id.et_parking)
        etAddress = root.findViewById(R.id.et_address)
    }

    private fun setupObserver() {
        propertyViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setUnitsSpinner(it)
            }
        })

        propertyViewModel.getResponseUnitsDetail().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                setContent(it)
            }
        })

        propertyViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setContent(it:ResponseUnitDetails) {
        try {
            etBuildingName.setText(it.body[0].buliding.name)
            etAccommodation.setText("Yes")
            etSpace.setText(it.body[0].space)
            etParking.setText(it.body[0].parking.toString())
            etAddress.setText(it.body[0].buliding.address)
        } catch (e: Exception) {
        }
    }

    private var unitId = 0
    private fun setUnitsSpinner(selections: ResponseTenantUnits) {
        val unitTitles = ArrayList<String>()
        for (item in selections.body) {
            unitTitles.add(item.name)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, unitTitles)
        unitsSpinner.setAdapter(adapter)
        unitsSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                val token = SavePref(requireContext()).getAccessToken()
                unitId = selections.body[position].id
                propertyViewModel.getUnitDetails(token, unitId)
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
}