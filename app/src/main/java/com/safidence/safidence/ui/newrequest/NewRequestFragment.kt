package com.safidence.safidence.ui.newrequest

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseRequestTypes
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class NewRequestFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var newRequestViewModel: NewRequestViewModel
    private lateinit var catSpinner: AutoCompleteTextView
    private lateinit var unitsSpinner: AutoCompleteTextView
    private lateinit var prioritySpinner: AutoCompleteTextView
    private lateinit var file: File
    private lateinit var etUpload: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etAvailable: TextInputEditText
    private lateinit var etDesc: TextInputEditText
    private lateinit var etSub: TextInputEditText
    private lateinit var btnSave: Button

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
        etUpload = root.findViewById(R.id.et_upload)
        etPhone = root.findViewById(R.id.et_no)
        etAvailable = root.findViewById(R.id.et_availability)
        etDesc = root.findViewById(R.id.et_desc)
        etSub = root.findViewById(R.id.et_sub)
        btnSave = root.findViewById(R.id.btn_save)

        etAvailable.setOnClickListener {
            updateErrorFields()
            selectTime()
        }

        etUpload.setOnClickListener {
            updateErrorFields()
            ImagePicker.with(requireActivity())
                .crop()
                .maxResultSize(1024, 1024, true)
                .createIntentFromDialog { launcher.launch(it) }
        }

        btnSave.setOnClickListener {
            updateErrorFields()
            val token = SavePref(requireContext()).getAccessToken()
            val sub:String = etSub.text.toString()
            val desc:String = etDesc.text.toString()
            val available:String = etAvailable.text.toString()
            val phone:String = etPhone.text.toString()
            if (!setErrorFields(sub, desc, available, phone))
                return@setOnClickListener
            newRequestViewModel.tenantRequest(token, catId, sub, desc, priorityValue, available, phone, unitId, file)
            showProgressDialog()
        }
    }

    private var catId = 0
    private fun setCatSpinner(selections: ResponseRequestTypes) {

        val catTitles = ArrayList<String>()
        for (item in selections.body) {
            catTitles.add(item.title)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, catTitles)
        catSpinner.setAdapter(adapter)

        catSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                catId = selections.body[position].id
                updateErrorFields()
            }
    }

    var unitId = 0
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
                unitId = selections.body[position].id
                updateErrorFields()
            }
    }

    var priorityValue = ""
    private fun setPrioritySpinner() {
        val selections = arrayOf("Normal", "Urgent")

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, selections)
        prioritySpinner.setAdapter(adapter)
        prioritySpinner.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                priorityValue = selections[position]
                updateErrorFields()
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

        newRequestViewModel.getTenantRequestResponse().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

        newRequestViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            etUpload.setText(uri.toString())
            file = uri.toFile()
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

    private fun updateErrorFields() {
        catSpinner.error = null
        unitsSpinner.error = null
        prioritySpinner.error = null
        etSub.error = null
        etDesc.error = null
        etAvailable.error = null
        etPhone.error = null
        etUpload.error = null
    }

    private fun setErrorFields(sub: String, desc: String, avail: String,
                               phone: String): Boolean {
        when {
            catId == 0 -> {
                catSpinner.error = "* Required"
                return false
            }
            unitId == 0 -> {
                unitsSpinner.error = "* Required"
                return false
            }
            sub == "" -> {
                etSub.error = "* Required"
                return false
            }
            desc == "" -> {
                etDesc.error = "* Required"
                return false
            }
            priorityValue == "" -> {
                prioritySpinner.error = "* Required"
                return false
            }
            avail == "" -> {
                etAvailable.error = "* Required"
                return false
            }
            phone == "" -> {
                etPhone.error = "* Required"
                return false
            }
            !this::file.isInitialized -> {
                etUpload.error = "* Required"
                return false
            }
            else -> return true
        }
    }


    private var requestDay = 0
    private var requestMonth: Int = 0
    private var requestYear: Int = 0
    private fun selectTime() {
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this@NewRequestFragment, year, month,day)
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        requestDay = dayOfMonth
        requestYear = year
        requestMonth = month
        val calendar: Calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(requireContext(), this@NewRequestFragment,
            hour, minute,
            DateFormat.is24HourFormat(requireContext()))
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        etAvailable.setText("$requestYear-$requestMonth-$requestDay $hourOfDay:$minute")
    }
}