package com.safidence.safidence.ui.cashpayment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.drjacky.imagepicker.ImagePicker
import com.safidence.safidence.R
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentCashPaymentBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import java.io.File

class CashPaymentFragment : Fragment() {

    private lateinit var cashPaymentViewModel: CashPaymentViewModel
    private lateinit var file: File
    private var _binding: FragmentCashPaymentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentCashPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews()
        setupObserver()

        return root
    }

    private fun setupViewModel() {
        cashPaymentViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(CashPaymentViewModel::class.java)
    }


    private fun initViews() {
        binding.etReceipt.setOnClickListener {
            updateErrorFields()
            ImagePicker.with(requireActivity())
                .crop()
                .maxResultSize(1024, 1024, true)
                .createIntentFromDialog { launcher.launch(it) }
        }

        binding.etDueDate.setText(arguments?.getString("due_date"))
        binding.etDuePayment.setText(arguments?.getString("due_payment"))
        binding.etPayTill.setText(arguments?.getString("pay_till"))
        val unitId = arguments?.getString("unit_id")
        binding.btnSave.setOnClickListener {
            updateErrorFields()
            if (setErrorFields()) {
                val token = SavePref(requireContext()).getAccessToken()
                val dueDate = binding.etDueDate.text.toString()
                val duePayment = binding.etDuePayment.text.toString()
                val paidTo = binding.etPaidTo.text.toString()

                if (unitId != null) {
                    cashPaymentViewModel.addCashPayment(
                        token,
                        "cash",
                        dueDate,
                        unitId,
                        duePayment,
                        paidTo,
                        file
                    )
                }
                showProgressDialog()
            }
        }
    }

    private fun setupObserver() {
        cashPaymentViewModel.getResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        })

        cashPaymentViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            // Use the uri to load the image
            binding.etReceipt.setText(uri.toString())
            file = uri.toFile()
        }
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
        binding.etPaidTo.error = null
        binding.etReceipt.error = null
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
            binding.etPaidTo.text.toString() == "" -> {
                binding.etPaidTo.error = "* Required"
                return false
            }
            !this::file.isInitialized -> {
                binding.etReceipt.error = "* Required"
                return false
            }
            else -> return true
        }
    }
}