package com.safidence.safidence.ui.creditcard

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentCreditCardBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import java.util.*

class CreditCardFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var creditCardViewModel: CreditCardViewModel
    private var _binding: FragmentCreditCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentCreditCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
        setupObserver()

        return root
    }

    private fun setupViewModel() {
        creditCardViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(CreditCardViewModel::class.java)
    }


    private fun initViews() {
        binding.etDueDate.setText(arguments?.getString("due_date"))
        binding.etDuePayment.setText(arguments?.getString("due_payment"))
        binding.etPayTill.setText(arguments?.getString("pay_till"))
        val unitId = arguments?.getString("unit_id")
        binding.btnSave.setOnClickListener {
            updateErrorFields()
            if (setErrorFields()) {
                val token = SavePref(requireContext()).getAccessToken()
                val dueDate = binding.etDueDate.text.toString()
                val payTill = binding.etPayTill.text.toString()
                val duePayment = binding.etDuePayment.text.toString()
                val cCNo = binding.etCcardNo.text.toString()
                val validTill = binding.etValidTill.text.toString()
                val sCode = binding.etCode.text.toString()

                if (unitId != null) {
                    creditCardViewModel.addCreditCardPayment(
                        token,
                        "card",
                        dueDate,
                        payTill,
                        unitId,
                        duePayment,
                        cCNo,
                        validTill,
                        sCode
                    )
                }
                showProgressDialog()
            }

        }

        binding.etValidTill.setOnClickListener {
            updateErrorFields()
            selectTime()
        }
    }

    private fun setupObserver() {
        creditCardViewModel.getResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        })

        creditCardViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun selectTime() {
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this@CreditCardFragment, year, month, day)
        datePickerDialog.show()
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateErrorFields() {
        binding.etDueDate.error = null
        binding.etPayTill.error = null
        binding.etDuePayment.error = null
        binding.etCcardNo.error = null
        binding.etValidTill.error = null
        binding.etCode.error = null
    }

    private fun setErrorFields(): Boolean {
        when {
            binding.etDueDate.text.toString() == "" -> {
                binding.etDueDate.error = "* Required"
                return false
            }
            binding.etPayTill.text.toString() == "" -> {
                binding.etPayTill.error = "* Required"
                return false
            }
            binding.etDuePayment.text.toString() == "" -> {
                binding.etDuePayment.error = "* Required"
                return false
            }
            binding.etCcardNo.text.toString() == "" -> {
                binding.etCcardNo.error = "* Required"
                return false
            }
            binding.etValidTill.text.toString() == "" -> {
                binding.etValidTill.error = "* Required"
                return false
            }
            binding.etCode.text.toString() == "" -> {
                binding.etCode.error = "* Required"
                return false
            }
            else -> return true
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.etValidTill.setText("$year-$month-$dayOfMonth")
    }
}