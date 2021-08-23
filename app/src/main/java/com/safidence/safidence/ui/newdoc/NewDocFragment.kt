package com.safidence.safidence.ui.newdoc

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
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
import com.safidence.safidence.data.model.ResponseDocTypes
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory
import java.io.File
import java.util.*

class NewDocFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var newDocViewModel: NewDocViewModel
    private lateinit var docTypesSpinner: AutoCompleteTextView
    private lateinit var etExpiry: TextInputEditText
    private lateinit var etNum: TextInputEditText
    private lateinit var etCountry: TextInputEditText
    private lateinit var file: File
    private lateinit var etUpload: TextInputEditText
    private lateinit var btnSave: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        newDocViewModel =
                ViewModelProvider(this).get(NewDocViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_doc, container, false)

        initViews(root)
        setupObserver()

        // call document types api
        newDocViewModel.getDocTypes(SavePref(requireContext()).getAccessToken())

        return root
    }

    private fun setupViewModel() {
        newDocViewModel = ViewModelProvider(
            this, ViewModelFactory(ApiHelper(ApiServiceImpl())))
            .get(NewDocViewModel::class.java)
    }

    private fun initViews(root: View) {
        docTypesSpinner = root.findViewById(R.id.ac_doc_type)
        etExpiry = root.findViewById(R.id.et_expiry)
        etUpload = root.findViewById(R.id.et_upload)
        etNum = root.findViewById(R.id.et_num)
        etCountry = root.findViewById(R.id.et_country)
        btnSave = root.findViewById(R.id.btn_save)

        etExpiry.setOnClickListener {
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

            if (!setErrorFields(etNum.text.toString(), etCountry.text.toString(),
                    etExpiry.text.toString()))
                return@setOnClickListener
            newDocViewModel.uploadDoc(SavePref(requireContext()).getAccessToken(), docTypeId,
                etNum.text.toString(), etCountry.text.toString(), etExpiry.text.toString(), file)
            showProgressDialog()
        }
    }

    private var docTypeId: Int = 0
    private fun setDocTypeSpinner(types: ResponseDocTypes) {
        val docTypesTitle = ArrayList<String>()
        for (item in types.data) {
            docTypesTitle.add(item.name)
        }

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, docTypesTitle)
        docTypesSpinner.setAdapter(adapter)
        docTypesSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                docTypeId = types.data[position].id
                updateErrorFields()
            }
    }

    private fun setupObserver() {
        newDocViewModel.getResponseDocTypes().observe(viewLifecycleOwner, Observer {
            setDocTypeSpinner(it)
        })

        newDocViewModel.getResponseUploadDoc().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

        newDocViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
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
        if (progressDialog != null)
            progressDialog.dismiss()
    }

    private fun selectTime() {
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this@NewDocFragment, year, month,day)
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        etExpiry.setText("$year-$month-$dayOfMonth")
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            etUpload.setText(uri.toString())
            file = uri.toFile()
        }
    }

    private fun updateErrorFields() {
        docTypesSpinner.error = null
        etExpiry.error = null
        etUpload.error = null
        etNum.error = null
        etCountry.error = null
    }

    private fun setErrorFields(num: String, country: String, expiry: String): Boolean {
        when {
            docTypeId == 0 -> {
                docTypesSpinner.error = "* Required"
                return false
            }
            num == "" -> {
                etNum.error = "* Required"
                return false
            }
            country == "" -> {
                etCountry.error = "* Required"
                return false
            }
            expiry == "" -> {
                etExpiry.error = "* Required"
                return false
            }
            !this::file.isInitialized -> {
                etUpload.error = "* Required"
                return false
            }
            else -> return true
        }
    }
}