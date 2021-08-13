package com.safidence.safidence.ui.newrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseRequestTypes
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory

class NewRequestFragment : Fragment() {

    private lateinit var newRequestViewModel: NewRequestViewModel
    private lateinit var catSpinner: AutoCompleteTextView
    private lateinit var unitsSpinner: AutoCompleteTextView
    private lateinit var prioritySpinner: AutoCompleteTextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        newRequestViewModel =
                ViewModelProvider(this).get(NewRequestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_request, container, false)

        initViews(root)
        setPrioritySpinner()

        setupObserver()
        newRequestViewModel.getRequestTypes(SavePref(requireContext()).getAccessToken())
        newRequestViewModel.getTenantUnits(SavePref(requireContext()).getAccessToken())

        return root
    }

    private fun setupViewModel() {
        newRequestViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(NewRequestViewModel::class.java)
    }

    private fun initViews(root: View) {
        catSpinner = root.findViewById(R.id.ac_tv_cat)
        unitsSpinner = root.findViewById(R.id.ac_tv_units)
        prioritySpinner = root.findViewById(R.id.ac_tv_priority)
    }

    private fun setCatSpinner(selections: ResponseRequestTypes) {

        val catTitles = ArrayList<String>()
        for (item in selections.body) {
            catTitles.add(item.title)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, catTitles)
        catSpinner.setAdapter(adapter)
        catSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setUnitsSpinner(selections: ResponseTenantUnits) {

        val unitTitles = ArrayList<String>()
        for (item in selections.body) {
            unitTitles.add(item.name)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, unitTitles)
        unitsSpinner.setAdapter(adapter)
        unitsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setPrioritySpinner() {
        val selections = arrayOf("Routine", "Urgent")

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, selections)
        prioritySpinner.setAdapter(adapter)
        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupObserver() {
        newRequestViewModel.getResponseReqTypes().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setCatSpinner(it)
            }
        })

        newRequestViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setUnitsSpinner(it)
            }
        })

        newRequestViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }
}