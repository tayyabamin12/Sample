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
import com.safidence.safidence.R
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseRequestTypes
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentNewRequestBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class NewRequestFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var newRequestViewModel: NewRequestViewModel
    private lateinit var file: File
    private var _binding: FragmentNewRequestBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentNewRequestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
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

    private fun initViews() {
        binding.etAvailability.setOnClickListener {
            updateErrorFields()
            selectTime()
        }

        binding.etUpload.setOnClickListener {
            updateErrorFields()
            ImagePicker.with(requireActivity())
                .crop()
                .maxResultSize(1024, 1024, true)
                .createIntentFromDialog { launcher.launch(it) }
        }

        binding.btnSave.setOnClickListener {
            updateErrorFields()
            val token = SavePref(requireContext()).getAccessToken()
            val sub:String = binding.etSub.text.toString()
            val desc:String = binding.etDesc.text.toString()
            val available:String = binding.etAvailability.text.toString()
            val phone:String = binding.etNo.text.toString()
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
        binding.acTvCat.setAdapter(adapter)

        binding.acTvCat.onItemClickListener =
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
        binding.acTvUnits.setAdapter(adapter)
        binding.acTvUnits.onItemClickListener =
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
        binding.acTvPriority.setAdapter(adapter)
        binding.acTvPriority.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                priorityValue = selections[position]
                updateErrorFields()
            }
    }

    private fun setupObserver() {
        newRequestViewModel.getResponseReqTypes().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setCatSpinner(it)
                binding.catLoading.visibility = View.GONE
            }
        })

        newRequestViewModel.getResponseTenantUnits().observe(viewLifecycleOwner, Observer{
            if (it.status == "success") {
                setUnitsSpinner(it)
                binding.unitsLoading.visibility = View.GONE
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
            binding.etUpload.setText(uri.toString())
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
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    private fun updateErrorFields() {
        binding.acTvCat.error = null
        binding.acTvUnits.error = null
        binding.acTvPriority.error = null
        binding.etSub.error = null
        binding.etDesc.error = null
        binding.etAvailability.error = null
        binding.etNo.error = null
        binding.etUpload.error = null
    }

    private fun setErrorFields(sub: String, desc: String, avail: String,
                               phone: String): Boolean {
        when {
            catId == 0 -> {
                binding.acTvCat.error = "* Required"
                return false
            }
            unitId == 0 -> {
                binding.acTvUnits.error = "* Required"
                return false
            }
            sub == "" -> {
                binding.etSub.error = "* Required"
                return false
            }
            desc == "" -> {
                binding.etDesc.error = "* Required"
                return false
            }
            priorityValue == "" -> {
                binding.acTvPriority.error = "* Required"
                return false
            }
            avail == "" -> {
                binding.etAvailability.error = "* Required"
                return false
            }
            phone == "" -> {
                binding.etNo.error = "* Required"
                return false
            }
            !this::file.isInitialized -> {
                binding.etUpload.error = "* Required"
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
        binding.etAvailability.setText("$requestYear-$requestMonth-$requestDay $hourOfDay:$minute")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}