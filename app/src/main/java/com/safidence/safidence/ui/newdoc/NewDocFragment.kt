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
import com.safidence.safidence.R
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.ResponseDocTypes
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentNewDocBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import com.safidence.safidence.utils.Util
import java.io.File
import java.util.*

class NewDocFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var newDocViewModel: NewDocViewModel
    private lateinit var file: File
    private var _binding: FragmentNewDocBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentNewDocBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
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

    private fun initViews() {
        setCountrySpinner()
        binding.etExpiry.setOnClickListener {
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

            if (!setErrorFields(binding.etNum.text.toString(), binding.macCountry.text.toString(),
                    binding.etExpiry.text.toString()))
                return@setOnClickListener
            newDocViewModel.uploadDoc(SavePref(requireContext()).getAccessToken(), docTypeId,
                binding.etNum.text.toString(), binding.macCountry.text.toString(), binding.etExpiry.text.toString(), file)
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
        binding.acDocType.setAdapter(adapter)
        binding.acDocType.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                docTypeId = types.data[position].id
                updateErrorFields()
            }
    }

    private fun setCountrySpinner() {
        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, Util.getCountryList())
        binding.macCountry.setAdapter(adapter)
        binding.macCountry.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                updateErrorFields()
            }
    }

    private fun setupObserver() {
        newDocViewModel.getResponseDocTypes().observe(viewLifecycleOwner, Observer {
            setDocTypeSpinner(it)
            binding.loading.visibility = View.GONE
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
        if (this::progressDialog.isInitialized)
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
        binding.etExpiry.setText("$year-$month-$dayOfMonth")
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            binding.etUpload.setText(uri.toString())
            file = uri.toFile()
        }
    }

    private fun updateErrorFields() {
        binding.acDocType.error = null
        binding.etExpiry.error = null
        binding.etUpload.error = null
        binding.etNum.error = null
        binding.macCountry.error = null
    }

    private fun setErrorFields(num: String, country: String, expiry: String): Boolean {
        when {
            docTypeId == 0 -> {
                binding.acDocType.error = "* Required"
                return false
            }
            num == "" -> {
                binding.etNum.error = "* Required"
                return false
            }
            country == "" -> {
                binding.macCountry.error = "* Required"
                return false
            }
            expiry == "" -> {
                binding.etExpiry.error = "* Required"
                return false
            }
            !this::file.isInitialized -> {
                binding.etUpload.error = "* Required"
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